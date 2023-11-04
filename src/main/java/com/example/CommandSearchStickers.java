package com.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandSearchStickers implements Action {
    private final String action;
    private String namePack;
    private final int MessageLimit = 20;

    public CommandSearchStickers(String action) {
        this.action = action;
    }

    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var text = "Введите название стикеров";

        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    //Метод для отправки паков пользователю
    @Override
    public SendMessage callback(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        namePack = msg.getText();


        Website combotSite = new Website("https://combot.org/telegram/stickers?q=" + namePack, "combotSticker");
        Website chpicSite = new Website("https://chpic.su/ru/stickers/search/" + namePack + "/?searchModule=stickers", "chpicSticker");
        
        //Сайт упал
        if (chpicSite.GetStatus() == false && combotSite.GetStatus() == false) 
            return new SendMessage(chatId, "Наблюдаются сбои в работе команды 🤧\nУже решаем проблему, ожидайте");
            
            // Создаем паки по отдельности
        // MapPack combotPack = new MapPack(combotSite);
        // MapPack chpicPack = new MapPack(chpicSite);
        
            // Создаем общий пак который содержит все стикеры из двух сайтов.
            // Общий пак содержит сначала стикеры из первого, а потом из второго сайта
        MapPack allPack = new MapPack(chpicSite, combotSite);
        
        String text;
        if (allPack.SizePack() == 0){
                text = "По вашему запросу не обнаружены стикеры 🤧"
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
