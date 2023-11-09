package com.example;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class AllButonReaction {
    Update update;
    //Пак со стикерами (уже заполненый из прошлой команды)
    MapPack allPack;

    //Счетчик страницы
    private int counterPage;
    //Количество паков на странице
    private int countPacksOnPage = 5;
    //Количество паков в массиве
    private int sizeAllPack;
    //Всего страниц
    private int countPage;


    public AllButonReaction(Update update, MapPack pack, int counterPage) {
        //Сначала выставляем все данные для работы кнопок
        if (pack!=null) allPack = pack;
        this.update = update;
        sizeAllPack = allPack.SizePack();
        countPage = (int) Math.ceil(sizeAllPack/countPacksOnPage);
        this.counterPage = counterPage;
    }

    public EditMessageText GetNewMessage() {
        String callBackData = update.getCallbackQuery().getData();
        //id сообщения которое нужно изменить
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        //id чата где изменяем сообщение
        long newChatId = update.getCallbackQuery().getMessage().getChatId();

        //Если нажата кнопка дальше
        if (callBackData.equals("nextButton")){
            //System.out.println("counterPage в классе: " + counterPage);
            //Вычисляем какие паки нужно выводить в зависимости от счетчика страницы
            int startElement = (counterPage * countPacksOnPage) - countPacksOnPage;
            int endElement = counterPage * countPacksOnPage;

            // System.out.println("startElement: " + startElement + "\n" +
            //                 "endElement: " + endElement + "\n");
            //Устанавливаем клавиатуру
            InlineKeyboardMarkup newKeyboard = SetKeyboard();

            //Изменяем сообщение
            final EditMessageText msg = new EditMessageText();
            msg.enableMarkdown(true);
            msg.setChatId(String.valueOf(newChatId));
            msg.setText(allPack.getPackInfo(startElement,endElement));
            msg.setMessageId((int)messageId);
            msg.setReplyMarkup(newKeyboard);
            msg.setParseMode("MarkdownV2");
            return msg;

        } else if (callBackData.equals("backButton")){ //Если нажата кнопка назад
            //System.out.println("counterPage в классе после уменьшения: " + counterPage);
            //Вычисляем какие паки нужно выводить в зависимости от счетчика страницы
            int startElement = (counterPage * countPacksOnPage) - countPacksOnPage;
            int endElement = counterPage * countPacksOnPage;

            // System.out.println("startElement: " + startElement + "\n" +
            //                 "endElement: " + endElement + "\n");
            //Устанавливаем клавиатуру
            InlineKeyboardMarkup newKeyboard = SetKeyboard();
            

            //Изменяем сообщение
            final EditMessageText msg = new EditMessageText();
            msg.enableMarkdown(true);
            msg.setChatId(String.valueOf(newChatId));
            msg.setText(allPack.getPackInfo(startElement,endElement));
            msg.setMessageId((int)messageId);
            msg.setReplyMarkup(newKeyboard);
            msg.setParseMode("MarkdownV2");
            return msg;

        } else if(callBackData.equals("page")){//Если нажата кнопка с выбранной страницей
            // AnswerCallbackQuery notify = new AnswerCallbackQuery();
            // System.out.println("messId: " + messageId);
            // notify.setCallbackQueryId(String.valueOf(messageId));
            // notify.setText(String.valueOf(counterPage) + "\\" + String.valueOf(countPage));
            return null;
        } else{
            return null;
        }
    }
    
    //Устанавливает клавиатуру в зависимости от номера страницы
    private InlineKeyboardMarkup SetKeyboard() {
        if (counterPage == 1) return SetBeginKeyboard();
        if (counterPage == countPage) return SetEndKeyboard();
        return SetMiddleKeyboard();
    }
    //Клавиатура с кнопками "Страница", "Дальше"
    private InlineKeyboardMarkup SetBeginKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton pageNumber = new InlineKeyboardButton();
        pageNumber.setText(String.valueOf(counterPage) + "\\" + String.valueOf(countPage));
        pageNumber.setCallbackData("page");

        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        nextButton.setText("➡️");
        nextButton.setCallbackData("nextButton");

        row1.add(pageNumber);
        row1.add(nextButton);
        rows.add(row1);
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }
    //Клавиатура с кнопками "Назад", "Страница", "Дальше"
    private InlineKeyboardMarkup SetMiddleKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("⬅️");
        backButton.setCallbackData("backButton");

        InlineKeyboardButton pageNumber = new InlineKeyboardButton();
        pageNumber.setText(String.valueOf(counterPage) + "\\" + String.valueOf(countPage));
        pageNumber.setCallbackData("page");

        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        nextButton.setText("➡️");
        nextButton.setCallbackData("nextButton");

        row1.add(backButton);
        row1.add(pageNumber);
        row1.add(nextButton);
        rows.add(row1);
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }
    //Клавиатура с кнопками "Назад", "Страница"
    private InlineKeyboardMarkup SetEndKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("⬅️");
        backButton.setCallbackData("backButton");

        InlineKeyboardButton pageNumber = new InlineKeyboardButton();
        pageNumber.setText(String.valueOf(counterPage) + "\\" + String.valueOf(countPage));
        pageNumber.setCallbackData("page");

        row1.add(backButton);
        row1.add(pageNumber);
        rows.add(row1);
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }
}
