package com.example.openaichatbot.telegram.handlers;

import com.example.openaichatbot.service.openai.OpenAi;
import com.example.openaichatbot.telegram.Bot;
import com.example.openaichatbot.telegram.keyboards.Keyboards;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImagesMessageHandler {

    private final OpenAi openAi;

    public SendMessage askPromptFromUser(Message message) {
        String messageText = "Введіть запит для генерації";
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text(messageText)
                .build();
        return sendMessage;
    }

    public SendMessage getPromptAndAskNumberOfImages(Message message) {
        String prompt = message.getText();
        Bot.context.put("prompt", prompt);
        String messageText = "Запит отримано. Введіть кількість зображень(макс. 3)";
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text(messageText)
                .build();
        return sendMessage;
    }

    public SendMediaGroup sendMediaGroup(Message message) {
        String prompt = Bot.context.get("prompt");
        int n = 1;
        String caption = "Ось результат до запиту \"" + prompt + "\". Можеш спробувати ще щось";
        try {
            n = Integer.parseInt(message.getText());
        } catch (Exception e) {
            caption += ". Ви ввели невірне число";
            log.error(e.getMessage());
        }
        List<String> photoLinks = openAi.generateImages(prompt, 0.5f, 1024, null, 0, true, n);
        List<InputMedia> medias = new ArrayList<>();
        String finalCaption = caption;
        int counter = 0;
        for (String link : photoLinks) {
            if (counter == 0) medias.add(InputMediaPhoto.builder().caption(finalCaption).media(link).build());
            else medias.add(InputMediaPhoto.builder().media(link).build());
            counter++;
        }
        counter = 0;
        ReplyKeyboardMarkup keyboard = Keyboards.createReplyMarkupKeyboard("Згенерувати зображення");
        SendMediaGroup sendMedia = SendMediaGroup.builder()
                .chatId(String.valueOf(message.getChatId()))
                .medias(medias)
                .build();
        return sendMedia;
    }
}