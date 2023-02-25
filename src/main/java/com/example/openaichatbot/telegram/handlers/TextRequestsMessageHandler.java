package com.example.openaichatbot.telegram.handlers;

import com.example.openaichatbot.dto.OpenAiResponseDto;
import com.example.openaichatbot.service.openai.OpenAiChat;
import com.example.openaichatbot.telegram.Bot;
import com.example.openaichatbot.telegram.keyboards.Keyboards;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class TextRequestsMessageHandler {

    private final OpenAiChat openAiChat;

    public SendMessage askPromptFromUser(Message message) {
        String messageText = "Введіть ваш запит";
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text(messageText)
                .build();
        return sendMessage;
    }

    public SendMessage getPromptAndReply(Message message) {
        String prompt = message.getText();
        Bot.context.put("textPrompt", prompt);
        OpenAiResponseDto response = openAiChat.getResponseFromChatGPT(prompt);
        String responseText = response.text();
        ReplyKeyboardMarkup keyboard = Keyboards.createReplyMarkupKeyboard("Завершити спілкування");
        if (response.finishReason().equals("length")) {
            responseText = "Відповідь занадто довга, все ,що можу дати нижче:\n" + responseText;
        }
        SendMessage sendMessage = new SendMessage().builder()
                .chatId(String.valueOf(message.getChatId()))
                .text(responseText)
                .replyMarkup(keyboard)
                .build();
        return sendMessage;
    }

    public SendMessage endChat(Message message) {
        String messageText = "Чат завершено";
        ReplyKeyboardMarkup keyboard = Keyboards.createReplyMarkupKeyboard("Згенерувати зображення", "Задати питання");
        SendMessage sendMessage = new SendMessage().builder()
                .chatId(String.valueOf(message.getChatId()))
                .text(messageText)
                .replyMarkup(keyboard)
                .build();
        return sendMessage;
    }
}
