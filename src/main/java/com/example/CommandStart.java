package com.example;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


public class CommandStart implements Action {

    

    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var out = new StringBuilder();
        out.append("Привет, ").append(msg.getFrom().getFirstName()).append("! 😀").append("\n\n").
        append("Я бот для поиска стикеров 😎").append("\n").
        append("Для использования выберите действие из списка меню 🔥");
        
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(out.toString());
        return sendMessage;
    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }
}
