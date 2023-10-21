package com.example;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
<<<<<<< HEAD
=======
        String logFilePath = "user_requests.log"; // путь к файлу
        GreetingBot bot = new GreetingBot(logFilePath);
>>>>>>> 29f45acec6571f9e9c007ed9c939c69d2c9f5d4e
        TelegramBotsApi BotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            BotsApi.registerBot(new GreetingBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        bot.close();
    }
}
