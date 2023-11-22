package com.example;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


public class CommandStart implements Action {
    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var out = new StringBuilder();
        out.append("Привет, ").append(msg.getFrom().getFirstName()).append("\\! 😀").append("\n").
        append("Я мультимедийный бот, который:").append("\n\n").
        append("🎯 Найдет новые стикеры и эмодзи\n\n").
        append("🖼 Увеличит разрешение фото\n\n").
        append("📤 Сконвертирует фото\n\n");
        
        //Создаем кнопки для сообщения
        InlineKeyboardMarkup keyboard = SetBeginKeyboard();

        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(out.toString());
        sendMessage.setReplyMarkup(keyboard);
        sendMessage.setParseMode("MarkdownV2");
        return sendMessage;
    }

    //Клавиатура с кнопками
    private InlineKeyboardMarkup SetBeginKeyboard() {
        
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        //Строки с кнопками
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();

        //Кнопка с поиском стикеров 
        InlineKeyboardButton searchStickersBtn = new InlineKeyboardButton();
        searchStickersBtn.setText("Найти стикер");
        searchStickersBtn.setCallbackData("searchStickersBtn");
        row1.add(searchStickersBtn);
        
        //Кнопка со случайным стикером
        InlineKeyboardButton randomStickersBtn = new InlineKeyboardButton();
        randomStickersBtn.setText("Случайный стикер");
        randomStickersBtn.setCallbackData("randomStickersBtn");
        row1.add(randomStickersBtn);

        //Кнопка с поиском эмоджи
        InlineKeyboardButton searchEmojiBtn = new InlineKeyboardButton();
        searchEmojiBtn.setText("Найти эмоджи");
        searchEmojiBtn.setCallbackData("searchEmojiBtn");
        row2.add(searchEmojiBtn);
        
        //Кнопка со случайным эмоджи
        InlineKeyboardButton randomEmojiBtn = new InlineKeyboardButton();
        randomEmojiBtn.setText("Случайный эмоджи");
        randomEmojiBtn.setCallbackData("randomEmojiBtn");
        row2.add(randomEmojiBtn);

        //Кнопка увеличить качество фото
        InlineKeyboardButton upscaleButton = new InlineKeyboardButton();
        upscaleButton.setText("Увеличить разрешение фото");
        upscaleButton.setCallbackData("upscalePhotoBtn");
        row3.add(upscaleButton);

        //Кнопка конвертировать фото
        InlineKeyboardButton convertPhotoBtn = new InlineKeyboardButton();
        convertPhotoBtn.setText("Конвертировать фото");
        convertPhotoBtn.setCallbackData("convertPhotoBtn");
        row4.add(convertPhotoBtn);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);

        keyboardMarkup.setKeyboard(rows);

        return keyboardMarkup;
    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }

    @Override
    public MapPack getPack() {
        // TODO Auto-generated method stub
        return null;
    }
}
