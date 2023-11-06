package com.example;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandBotInfo implements Action {
    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        
        var text = new StringBuilder();
        text.append("Данный бот разработан @GanzaPaz и @Singingsinking, двумя студентами Санкт-Петербургского государственного электротехнического университета «ЛЭТИ».").append("\n");
        text.append("Если по вашему запросу бот не находит нужные стикеры, попробуйте изменить запрос или использовать другой язык для поиска.");
        
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text.toString());
        return sendMessage;
    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }

    @Override
    public MapPack getPack() {
        return null;
    }
}
