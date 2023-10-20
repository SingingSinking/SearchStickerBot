package com.example;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        
        

        
        TelegramBotsApi BotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            BotsApi.registerBot(new GreetingBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
