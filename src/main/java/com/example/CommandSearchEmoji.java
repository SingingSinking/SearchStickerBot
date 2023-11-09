package com.example;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class CommandSearchEmoji implements Action{
    private final String action;
    private String namePack;
    private MapPack allPack;

    //Сколько еможди выводится на одной странице, следует учитывать что при нажатии кнопки, переменная в AllButtonReation
    //Может быть другой, тогда на страницах будет выводиться разное количесво паков
    private int countPacksOnPage = 5;
    //Количество паков в массиве
    private int sizeAllPack;
    //Всего страниц
    private int countPage;


    public CommandSearchEmoji(String action) {
        this.action = action;
    }

    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var text = "Введите название эмоджи";

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
        allPack = new MapPack(chpicSite);
        
        sizeAllPack = allPack.SizePack();

        countPage = (int) Math.ceil(sizeAllPack/countPacksOnPage);
        if (sizeAllPack < countPacksOnPage) countPage = 1;
        //Создаем кнопки для перелистывания меню
        InlineKeyboardMarkup keyboard = SetBeginKeyboard();


        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(keyboard);
        String text;
        if (sizeAllPack == 0){
            text = "К сожалению, по вашему запросу ничего не найдено\\. 🤧\n\n" + 
                    "Почему эмоджи могут не находиться?\n\n" +
                    "1\\. Убедитесь, что вы ввели правильное название набора с эмоджи\\.\n\n" +
                    "2\\. Если вы уверены в правильности названия, попробуйте использовать другой язык для поиска\\.";
            sendMessage.setReplyMarkup(null);
        } else 
            text =  allPack.getPackInfo(0, countPacksOnPage);

        sendMessage.setText(text);
        sendMessage.setParseMode("MarkdownV2");
        //System.out.println(sendMessage);
        return sendMessage;
    }

    //Клавиатура с кнопками "Страница", "Дальше"
    private InlineKeyboardMarkup SetBeginKeyboard() {
        
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton pageNumber = new InlineKeyboardButton();
        pageNumber.setText("1" + "\\" + String.valueOf(countPage));
        pageNumber.setCallbackData("page");
        row1.add(pageNumber);
        if (countPage > 1){
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText("➡️");
            nextButton.setCallbackData("nextButton");
            row1.add(nextButton);
        }

        rows.add(row1);
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    @Override
    public MapPack getPack() {
        // TODO Auto-generated method stub
        return allPack;
    }
}
