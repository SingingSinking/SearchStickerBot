package com.example;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandUpscalePhoto implements Action {
    @Override
    public SendMessage handle(Update update) {
        var msg = update.getCallbackQuery().getMessage();
        var chatId = msg.getChatId().toString();
        
        var text = new StringBuilder();
        text.append("Данная функция пока не доступна");
        
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
