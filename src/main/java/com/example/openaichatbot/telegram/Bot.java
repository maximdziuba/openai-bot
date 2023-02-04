package com.example.openaichatbot.telegram;

import com.example.openaichatbot.telegram.handlers.CommonMessageHandler;
import com.example.openaichatbot.telegram.handlers.ImagesMessageHandler;
import com.example.openaichatbot.telegram.states.ImagesHandlerState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final CommonMessageHandler commonMessageHandler;
    private final ImagesMessageHandler imagesMessageHandler;
    public static ImagesHandlerState imagesHandlerState;
    public static Map<String, String> context = new HashMap<>();

    @Value("${telegram.bot.username}")
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

    private void handleIncomingMessage(Message message) throws TelegramApiException {
        var messageText = message.getText();
        var sendMessage = new SendMessage();
        switch (messageText) {
            case "/start":
                sendMessage = commonMessageHandler.greeting(message);
                execute(sendMessage);
                break;
            case "/generate":
                sendMessage = imagesMessageHandler.askPromptFromUser(message);
                execute(sendMessage);
                imagesHandlerState = ImagesHandlerState.PROMPT_ASKED;
                break;
            default:
                if (message.hasText() && Objects.equals(imagesHandlerState, ImagesHandlerState.PROMPT_ASKED)) {
                    var sendPhoto = imagesMessageHandler.sendImage(message);
                    execute(sendPhoto);
                    imagesHandlerState = ImagesHandlerState.GOT_PROMPT_AND_ASKED_NUMBER;
                }
        }
    }

}