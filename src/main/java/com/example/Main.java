package com.example;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        String logFilePath = "user_requests.log"; // Заменить путь на путь к вашему файлу
        GreetingBot bot = new GreetingBot(logFilePath);
        TelegramBotsApi BotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            BotsApi.registerBot(new GreetingBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        bot.close();
    }
}
