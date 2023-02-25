package com.example.openaichatbot.telegram.handlers;

import com.example.openaichatbot.telegram.keyboards.Keyboards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class CommonMessageHandler {


    public SendMessage greeting(Message message) {
        String name = message.getChat().getFirstName();
        String messageText = "Привіт, " + name + "! Натисни кнопку і напиши що я можу для тебе згенерувати";
        ReplyKeyboardMarkup keyboard = Keyboards.createReplyMarkupKeyboard("Згенерувати зображення", "Задати питання");
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text(messageText)
                .replyMarkup(keyboard)
                .build();
        return sendMessage;
    }
}
