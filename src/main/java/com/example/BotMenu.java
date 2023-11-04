package com.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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

        //Команды меню
        List<BotCommand> listOfButtonsMenu = new ArrayList<>();
        listOfButtonsMenu.add(new BotCommand("/start", "Приветственное сообщение"));
        listOfButtonsMenu.add(new BotCommand("/searchsticker", "Начать поиск стикер-пака"));
        listOfButtonsMenu.add(new BotCommand("/randomsticker", "Случайный стикер-пак"));
        listOfButtonsMenu.add(new BotCommand("/searchemoji", "Поиск эмоджи по слову"));
        listOfButtonsMenu.add(new BotCommand("/randomemoji", "Случайный эмоджи"));
        listOfButtonsMenu.add(new BotCommand("/botinfo", "Информация о боте"));

        try {
            this.execute(new SetMyCommands(listOfButtonsMenu, new BotCommandScopeDefault(), null));
        } catch(TelegramApiException e){
            System.out.println("Error create a buttons for menu: " + e.getMessage());
        }

    }


    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String key = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            //Забираем данные о пользователе
            AddUserInfoToLog(update);

            //Для всех команд используем стандартное меню
            //В зависимости от команды, нужно в соответствующем классе изменить ReplyKeyboardMarkup
            ReplyKeyboardMarkup MainMenuKeyboard = GetMainMenuKeyboard();

            if (actions.containsKey(key)) {
                SendMessage msg = actions.get(key).handle(update);

                msg.setReplyMarkup(MainMenuKeyboard);

                bindingBy.put(chatId, key);
                send(msg);
            } else if (bindingBy.containsKey(chatId)) {
                SendMessage msg = actions.get(bindingBy.get(chatId)).callback(update);

                msg.setReplyMarkup(MainMenuKeyboard);
                
                bindingBy.remove(chatId);
                send(msg);
            } 
        }
    }

    private ReplyKeyboardMarkup GetMainMenuKeyboard() {
        
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        row1.add(new KeyboardButton("Поиск стикеров"));
        row1.add(new KeyboardButton("Случайный стикер"));
        row2.add(new KeyboardButton("Поиск эмоджи"));
        row2.add(new KeyboardButton("Случайный эмоджи"));
        row3.add(new KeyboardButton("Информация о боте"));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
    //Метод записи пользователя в лог
    private void AddUserInfoToLog(Update update) {
        String username = update.getMessage().getFrom().getUserName(); // Получение username пользователя
        String firstName =  update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();
        String userLangCode =  update.getMessage().getFrom().getLanguageCode();
        String userRequest =  update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        requestsLogger.logUserRequest(username, firstName, lastName, chatId, userLangCode, userRequest); // Запись пользователя в лог
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
