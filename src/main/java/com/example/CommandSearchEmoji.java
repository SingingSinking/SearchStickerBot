package com.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandSearchEmoji implements Action{
    private final String action;
    private String namePack;
    private final int MessageLimit = 20;

    public CommandSearchEmoji(String action) {
        this.action = action;
    }

    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var text = "Введите название емоджи";

        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    //Метод для емоджи паков пользователю
    @Override
    public SendMessage callback(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        namePack = msg.getText();

        //Сайт с Emoji (аргумент метода - chpicEmoji, по нему MapPack понимает что с сайта берутся емоджи)
        Website chpicSite = new Website("https://chpic.su/ru/stickers/search/" + namePack + "/?searchModule=emojis", "chpicEmoji");
        //Сайт упал
        if (chpicSite.GetStatus() == false) 
            return new SendMessage(chatId, "Наблюдаются сбои в работе команды 🤧\nУже решаем проблему, ожидайте");
        
            // Создаем общий пак который содержит все емоджи
        MapPack allPack = new MapPack(chpicSite);
        
        

        String text;
        if (allPack.SizePack() == 0){
                text = "По вашему запросу не обнаружены эмоджи 🤧"
                + "\nВозможно, вам стоит немного изменить текст сообщения или попробовать другой язык";
        } else {
                text =  allPack.getPackInfo(MessageLimit);
        }

        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }
}
