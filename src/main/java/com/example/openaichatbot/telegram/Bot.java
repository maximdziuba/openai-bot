package com.example.openaichatbot.telegram;

import com.example.openaichatbot.telegram.handlers.CommonMessageHandler;
import com.example.openaichatbot.telegram.handlers.ImagesMessageHandler;
import com.example.openaichatbot.telegram.handlers.TextRequestsMessageHandler;
import com.example.openaichatbot.telegram.states.ImagesHandlerState;
import com.example.openaichatbot.telegram.states.TextHandlerState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final CommonMessageHandler commonMessageHandler;
    private final ImagesMessageHandler imagesMessageHandler;
    private final TextRequestsMessageHandler textRequestsMessageHandler;
    public static ImagesHandlerState imagesHandlerState;
    public static TextHandlerState textHandlerState;
    public static Map<String, String> context = new HashMap<>();

    @Value("${bot_username}")
    private String username;

    @Value("${bot_token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                handleIncomingMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void handleIncomingMessage(Message message) throws TelegramApiException, FileNotFoundException {
        var messageText = message.getText();
        var sendMessage = new SendMessage();
        switch (messageText) {
            case "/start":
                sendMessage = commonMessageHandler.greeting(message);
                execute(sendMessage);
                break;
            case "Згенерувати зображення":
                sendMessage = imagesMessageHandler.askPromptFromUser(message);
                execute(sendMessage);
                imagesHandlerState = ImagesHandlerState.PROMPT_ASKED;
                break;
            case "Задати питання":
                sendMessage = textRequestsMessageHandler.askPromptFromUser(message);
                execute(sendMessage);
                textHandlerState = TextHandlerState.ASKED_PROMPT;
                break;
            default:
                if (message.hasText() && Objects.equals(imagesHandlerState, ImagesHandlerState.PROMPT_ASKED)) {
                    sendMessage = imagesMessageHandler.getPromptAndAskNumberOfImages(message);
                    execute(sendMessage);
                    imagesHandlerState = ImagesHandlerState.GOT_PROMPT_AND_NUMBER;
                    break;
                } else if (message.hasText() && Objects.equals(imagesHandlerState, ImagesHandlerState.GOT_PROMPT_AND_NUMBER)) {
                    SendMediaGroup sendMediaGroup = imagesMessageHandler.sendMediaGroup(message);
                    execute(sendMediaGroup);
                    imagesHandlerState = ImagesHandlerState.GOT_PROMPT_AND_NUMBER;
                } else if (message.hasText() && Objects.equals(textHandlerState, TextHandlerState.ASKED_PROMPT) && messageText.equals("Завершити спілкування")) {
                    sendMessage = textRequestsMessageHandler.endChat(message);
                    execute(sendMessage);
                    textHandlerState = null;
                } else if (message.hasText() && Objects.equals(textHandlerState, TextHandlerState.ASKED_PROMPT)) {
                    sendMessage = textRequestsMessageHandler.getPromptAndReply(message);
                    execute(sendMessage);
                }
        }
    }

}