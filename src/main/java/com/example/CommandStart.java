package com.example;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public class CommandStart implements Action {

    

    @Override
    public BotApiMethod handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var out = new StringBuilder();
        out.append("Привет!").append("\n").
        append("Я бот для поиска стикеров.").append("\n").
        append("Для использования выберите действие из списка меню.");
        return new SendMessage(chatId, out.toString());
    }

    @Override
    public BotApiMethod callback(Update update) {
        return handle(update);
    }
}
