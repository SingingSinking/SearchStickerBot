package com.example;

import java.util.ArrayList;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandRandomSticker implements Action{
    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();

        //Создаем массив рандомных слов
        ArrayList<String> randomWords = GetRandomWord();

        if (randomWords == null) return new SendMessage(chatId, "Наблюдаются сбои в работе команды 🤧\nУже решаем проблему, ожидайте");

        //Получаем паки по первому слову из randomWords
        MapPack allPack = GetPackByWord(randomWords.get(0));

        if (allPack == null) return new SendMessage(chatId, "Наблюдаются сбои в работе команды 🤧\nУже решаем проблему, ожидайте");
        
        //Если по первому слову не нашлось набора со стикерами
        if (allPack.SizePack() == 0){

            //Удаляем первое слово потому что по нему не нашлось паков
            randomWords.remove(0);
            
            //Подставляем слова пока не найдется хотя бы один набор стикеров
            for (String word : randomWords) {
                allPack = GetPackByWord(word);
                //Выход из цикла если нашелся хотя бы один пак
                if (allPack.SizePack()!=0) break;
            }
        }
        var out = new StringBuilder();
        out.append("Новый стикер-пак для вас 😋:").append("\n");
        out.append("Название: ").append(allPack.GetNamePack(0)).append("\n");
        out.append("Ссылка на добавление: ").append(allPack.GetUrlPack(0)).append("\n");

        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(out.toString());
        return sendMessage;
    }
    //
    private MapPack GetPackByWord(String randomWords) {
        Website combotSite = new Website("https://combot.org/telegram/stickers?q=" + randomWords, "combotSticker");
        Website chpicSite = new Website("https://chpic.su/ru/stickers/search/" + randomWords + "/?searchModule=stickers", "chpicSticker");
        if (chpicSite.GetStatus() == false && combotSite.GetStatus() == false) 
            return null;
        return new MapPack(chpicSite, combotSite);
    }

    private ArrayList<String> GetRandomWord() {
        //Сайт с рандомными словами (каждый раз разные слова по одной и той же ссылке)
        //kreeklySite всегда возвращает 20 слов
        Website kreeklySite = new Website("https://www.kreekly.com/random/noun/", "kreekly");

        ArrayList<String> words = new ArrayList<>();
        if (kreeklySite.GetStatus() == true){
            int countWords = kreeklySite.GetAllHtmlPage().getElementsByClass("dict-word").size();
            for (int i = 0; i<countWords; i++){

                String word =kreeklySite.GetAllHtmlPage().
                getElementsByClass("dict-word").
                get(i).child(2).text();

                words.add(word);
            }
        } else return null;

        return words;
    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }
}
