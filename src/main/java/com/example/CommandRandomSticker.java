package com.example;

import java.util.ArrayList;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandRandomSticker implements Action{
    private MapPack allPack;
    
    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();

        //Создаем массив рандомных слов
        ArrayList<String> randomWords = GetRandomWord();

        if (randomWords == null) return new SendMessage(chatId, "Наблюдаются сбои в работе команды 🤧\nУже решаем проблему, ожидайте");

        //Получаем паки по первому слову из randomWords
        allPack = GetPackByWord(randomWords.get(0));

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
        
        StringBuilder info = new StringBuilder();
        String stickerUrl = allPack.GetUrlPack(0);
        String stickerName =allPack.GetNamePack(0);

        //Экранируем все спец. символы, иначе телеграм не будет их учитывать и ссылка потеряет часть символов
        stickerUrl = ShieldStr(stickerUrl);
        stickerName = ShieldStr(stickerName);

        info.append("Новые стикеры для вас 😋:").append("\n");
        info.append("👉Название: ").append(stickerName).append("\n");
        info.append("   [\\[Открыть\\]]").append("(" + stickerUrl + ")").append("\n");
        info.append("\n");
        // info.append("URL image: ").append(pack.GetImg()).append("\n\n");
        
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setParseMode("MarkdownV2");
        sendMessage.setText(info.toString());
        return sendMessage;
    }
    
    //метод экранирует строку
    private String ShieldStr(String str){
        return str.replaceAll("\\(", "%28").replaceAll("\\)", "%29").replaceAll("_", "%5f").replace(".", "\\.");
    }

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
    @Override
    public MapPack getPack() {
        // TODO Auto-generated method stub
        return allPack;
    }
}
