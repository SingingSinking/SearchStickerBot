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

        //Создаем массив из 100 рандомных слов
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

                if (allPack.SizePack()!=0) break;
            }
            //Если после подстановки 100 слов не нашлись паки, выводим первый пак по запросу anime (Крайне редкий шанс что такое произойдет)
            combotSite = new Website("https://combot.org/telegram/stickers?q=" + "anime", "combot");
            chpicSite = new Website("https://chpic.su/ru/stickers/search/" + "anime" + "/?searchModule=stickers", "chpic");
            allPack = new MapPack(chpicSite, combotSite);
        }

        var out = new StringBuilder();
        out.append("Название: ").append(allPack.GetNamePack(0)).append("\n");
        out.append("Ссылка на добавление: ").append(allPack.GetUrlPack(0)).append("\n");

        return new SendMessage(chatId, out.toString());
    }

    private ArrayList<String> GetRandomWord() {
        //Сайт с рандомными словами (каждый раз разные слова по одной и той же ссылке)
        Website sanstvSite = new Website("https://sanstv.ru/randomWord/lang-en/strong-2/count-100/word-%3F%3F%3F%3F%3F%3F", "sanstv");

        ArrayList<String> words = new ArrayList<>();
        if (sanstvSite.GetAllHtmlPage() != null){
            //System.out.println(sanstvSite.GetAllHtmlPage().getElementsByTag("li").size());
            int countWords = sanstvSite.GetAllHtmlPage().getElementsByTag("li").size();
            for (int i = 0; i<countWords; i++){
                String word =
                sanstvSite.GetAllHtmlPage().getElementsByTag("li").get(i).child(0).text();
                //System.out.println(word);
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
