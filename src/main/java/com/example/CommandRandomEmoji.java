package com.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandRandomEmoji implements Action{
    
    @Override
    public SendMessage handle(Update update) {
        var msg = update.getCallbackQuery().getMessage();
        var chatId = msg.getChatId().toString();

        //Случайные номаера страницы и пака на странице
        int randomNumPage = 1 + (int) ( Math.random() * 79 );
        int randomNumPack = 1 + (int) ( Math.random() * 19 );

        Website chpicEmoji = new Website("https://chpic.su/ru/emojis/?page=" + randomNumPage, "chpicRandomEmoji");
        if (chpicEmoji.GetStatus() == false) 
            return new SendMessage(chatId, "Наблюдаются сбои в работе команды 🤧\nУже решаем проблему, ожидайте");

        //Парсер
        Pack pack =  GetRandomPack(chpicEmoji, randomNumPack);

        StringBuilder info = new StringBuilder();
        String stickerUrl = pack.GetUrl();
        String stickerName = pack.GetName();

        //Экранируем все спец. символы, иначе телеграм не будет их учитывать и ссылка потеряет часть символов
        stickerUrl = ShieldStr(stickerUrl);
        stickerName = ShieldStr(stickerName);

        info.append("Новые эмоджи для вас 😋:").append("\n");
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
    


    private Pack GetRandomPack(Website htmlDom, int randomNumPack) {
        String urlPack =
                    "t.me/addemoji/" + htmlDom.GetAllHtmlPage().getElementsByClass("titleStr").get(randomNumPack).child(1).getElementsByTag("a").attr("href");
        urlPack = urlPack.replace("/ru/emojis/", "");
        urlPack = urlPack.substring(0, urlPack.length() - 1);

        String namePack = htmlDom.GetAllHtmlPage().getElementsByClass("titleStr").get(randomNumPack).child(1)
                    .text();

        // String urlImgPack = "https://chpic.su"
        //         + htmlDom.GetAllHtmlPage().getElementsByClass("images").get(randomNumPack).select("img").attr("src");

        return new Pack(namePack, urlPack, null);
    }



    //метод экранирует строку
    private String ShieldStr(String str){
        return str.replaceAll("\\(", "%28")
        .replaceAll("\\)", "%29")
        .replaceAll("_", "%5f")
        .replace(".", "\\.")
        .replaceAll("-", "%2d")
        .replaceAll("\\|", "%7c")
        .replaceAll("#", "%23")
        .replaceAll("!", "%21");
    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }

    @Override
    public MapPack getPack() {
        // TODO Auto-generated method stub
        return null;
    }
}
