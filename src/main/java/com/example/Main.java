package com.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws TelegramApiException, IOException {
        var tg = new TelegramBotsApi(DefaultBotSession.class);
        String logFilePath = "user_requests.log"; // путь к файлу

        var actions = Map.of(
                "/start", new CommandStart(),
                "Поиск стикеров", new CommandSearchStickers("/search"),
                "Случайный стикер", new CommandRandomSticker(),
                "Поиск эмоджи", new CommandSearchEmoji("/search"),
                "Случайный эмоджи", new CommandRandomEmoji(),
                "Информация о боте", new CommandBotInfo()
        );
        tg.registerBot(new BotMenu(actions, "@Search_Stiker_bot", "6670951712:AAG7SQr3bB0soMaaZIW0xcjr6skoXlg4LS4", logFilePath));
    }
}
