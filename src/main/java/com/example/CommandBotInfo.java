package com.example;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandBotInfo implements Action {
    @Override
    public BotApiMethod handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var text = "Данный бот разработан @GanzaPaz и @Singing_sinking, двумя студентами Санкт-Петербургского государственного электротехнического университета «ЛЭТИ». \n" + 
        "Если по вашему запросу бот не находит нужные стикеры, попробуйте изменить запрос или использовать другой язык для поиска.";
        return new SendMessage(chatId, text);
    }

    @Override
    public BotApiMethod callback(Update update) {
        return handle(update);
    }
}
