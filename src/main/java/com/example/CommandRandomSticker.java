package com.example;

import java.util.ArrayList;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandRandomSticker implements Action{
    @Override
    public BotApiMethod handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();

        //Создаем массив из countWords рандомных слов
        //При значении больше 200, начинают возникать сбои в работе 
        ArrayList<String> randomWords = GetRandomWord();

        //Поиск пака по первому слову
        Website combotSite = new Website("https://combot.org/telegram/stickers?q=" + randomWords.get(0), "combot");
        Website chpicSite = new Website("https://chpic.su/ru/stickers/search/" + randomWords.get(0) + "/?searchModule=stickers", "chpic");
        MapPack allPack = new MapPack(chpicSite, combotSite);

        //Если по первому слову не нашлось набора со стикерами
        if (allPack.SizePack() == 0){
            //Подставляем слова пока не найдется хотя бы один набор стикеров
            for (String word : randomWords) {
                combotSite = new Website("https://combot.org/telegram/stickers?q=" + word, "combot");
                chpicSite = new Website("https://chpic.su/ru/stickers/search/" + word + "/?searchModule=stickers", "chpic");
                allPack = new MapPack(chpicSite, combotSite);
                System.out.println(allPack.GetNamePack(0));
                if (allPack.SizePack()!=0) break;
            }
            //Если после подстановки countWords слов не нашлись паки, 
            //выводим первый пак по запросу anime (Крайне редкий шанс что такое произойдет при большом countWords)
            combotSite = new Website("https://combot.org/telegram/stickers?q=" + "anime", "combot");
            chpicSite = new Website("https://chpic.su/ru/stickers/search/" + "anime" + "/?searchModule=stickers", "chpic");
            allPack = new MapPack(chpicSite, combotSite);
        }
        //System.out.println(allPack.SizePack());
        var out = new StringBuilder();
        out.append("Новый стикер-пак для вас 😋:").append("\n");
        out.append("Название: ").append(allPack.GetNamePack(0)).append("\n");
        out.append("Ссылка на добавление: ").append(allPack.GetUrlPack(0)).append("\n");


        return new SendMessage(chatId, out.toString());
    }

    private ArrayList<String> GetRandomWord() {
        //Сайт с рандомными словами (каждый раз разные слова по одной и той же ссылке)
        Website kreeklySite = new Website("https://www.kreekly.com/random/noun/", "kreekly");

        ArrayList<String> words = new ArrayList<>();
        if (kreeklySite.GetAllHtmlPage() != null){
            //System.out.println(sanstvSite.GetAllHtmlPage().getElementsByTag("li").size());
            int countWords = kreeklySite.GetAllHtmlPage().getElementsByClass("dict-word").size();
            for (int i = 0; i<countWords; i++){
                String word =
                kreeklySite.GetAllHtmlPage().getElementsByClass("dict-word").get(i).child(2).text();
                System.out.println(word);
                words.add(word);
            }
        }

        return words;
    }

    @Override
    public BotApiMethod callback(Update update) {
        return handle(update);
    }
}
