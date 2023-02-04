package com.example.openaichatbot.telegram.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class CommonMessageHandler {


    public SendMessage greeting(Message message) {
        var name = message.getChat().getFirstName();
        var messageText = "Привіт, " + name + "!";
        var sendMessage = SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text(messageText)
                .build();
        return sendMessage;
    }
}
