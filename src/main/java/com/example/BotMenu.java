package com.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BotMenu extends TelegramLongPollingBot {
    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private final Map<String, Action> actions;
    private final String username;
    private final String token;
    private UserRequestsLogger requestsLogger;

    public BotMenu(Map<String, Action> actions, String username, String token, String logFilePath) {
        this.actions = actions;
        this.username = username;
        this.token = token;
        requestsLogger = new UserRequestsLogger(logFilePath);

        //Кнопки меню
        List<BotCommand> listOfButtonsMenu = new ArrayList<>();
        listOfButtonsMenu.add(new BotCommand("/start", "Приветственное сообщение"));
        listOfButtonsMenu.add(new BotCommand("/search", "Начать поиск стикер-пака"));
        listOfButtonsMenu.add(new BotCommand("/botinfo", "Информация о боте"));
        try {
            this.execute(new SetMyCommands(listOfButtonsMenu, new BotCommandScopeDefault(), null));
        } catch(TelegramApiException e){
            System.out.println("Error create a buttons for menu: " + e.getMessage());
        }

    }


    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {

            AddUserInfoToLog(update);
            String key = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            if (actions.containsKey(key)) {
                BotApiMethod msg = actions.get(key).handle(update);
                bindingBy.put(chatId, key);
                send(msg);
            } else if (bindingBy.containsKey(chatId)) {
                var msg = actions.get(bindingBy.get(chatId)).callback(update);
                bindingBy.remove(chatId);
                send(msg);
            }
        }
    }
    //Метод записи пользователя в лог
    private void AddUserInfoToLog(Update update) {
        String username = update.getMessage().getFrom().getUserName(); // Получение username пользователя
        String firstName =  update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();
        String userLangCode =  update.getMessage().getFrom().getLanguageCode();
        String userRequest =  update.getMessage().getText();
        requestsLogger.logUserRequest(username, firstName, lastName, userLangCode, userRequest); // Запись пользователя в лог
    }

    private void send(BotApiMethod msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return username;
    }

    public String getBotToken() {
        return token;
    }
}
