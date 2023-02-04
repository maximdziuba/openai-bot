package com.example.openaichatbot.telegram.handlers;

import com.example.openaichatbot.service.openai.OpenAi;
import com.example.openaichatbot.telegram.keyboards.Keyboards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class ImagesMessageHandler {

    private final OpenAi openAi;

    public SendMessage askPromptFromUser(Message message) {
        var messageText = "Введіть запит для генерації";
        var sendMessage = SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text(messageText)
                .build();
        return sendMessage;
    }

    public SendPhoto sendImage(Message message) {
        var prompt = message.getText();
        var caption = "Ось результат до запиту \"" + prompt + "\". Можеш спробувати ще щось";
        var photoLink = openAi.generateImage(prompt, 0.5f, 1024, null, 0, true, 1);
        var keyboard = Keyboards.createReplyMarkupKeyboard("Згенерувати зображення");
        var sendPhoto = SendPhoto.builder()
                .chatId(String.valueOf(message.getChatId()))
                .photo(new InputFile(photoLink))
                .caption(caption)
                .replyMarkup(keyboard)
                .build();
        return sendPhoto;
    }
}