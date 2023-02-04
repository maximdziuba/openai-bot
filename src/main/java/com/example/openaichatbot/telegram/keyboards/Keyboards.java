package com.example.openaichatbot.telegram.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;

public class Keyboards {
    public static ReplyKeyboardMarkup createReplyMarkupKeyboard(String... rows) {
        var keyboard = new ReplyKeyboardMarkup();
        var row = new KeyboardRow();
        for (var el : rows) {
            row.add(el);
            if (row.size() == 2) {
                createReplyMarkupKeyboard(Arrays.copyOfRange(rows, 2, rows.length));
            }
        }
        keyboard.setKeyboard(List.of(row));
        keyboard.setResizeKeyboard(true);
        return keyboard;
    }
}
