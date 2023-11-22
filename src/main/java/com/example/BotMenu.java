package com.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BotMenu extends TelegramLongPollingBot {
    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private final Map<String, Action> actionsCommand;
    private final String username;
    private final String token;
    private UserRequestsLogger requestsLogger;

    public BotMenu(Map<String, Action> actionsCommand, String username, String token, String logFilePath) {
        this.actionsCommand = actionsCommand;
        this.username = username;
        this.token = token;
        requestsLogger = new UserRequestsLogger(logFilePath);

        //Команды меню
        List<BotCommand> listOfButtonsMenu = new ArrayList<>();
        listOfButtonsMenu.add(new BotCommand("/start", "Что умеет бот?"));
        listOfButtonsMenu.add(new BotCommand("/botinfo", "Информация о боте"));

        try {
            this.execute(new SetMyCommands(listOfButtonsMenu, new BotCommandScopeDefault(), null));
        } catch(TelegramApiException e){
            System.out.println("Error create a buttons for menu: " + e.getMessage());
        }

    }


    String commandForCallBack = "/start";
    int counterPage;

    public void onUpdateReceived(Update update) {
        //Если пришло какое-то сообщение
        if (update.hasMessage()) {
            String selectedCommand = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            //Забираем данные о пользователе
            AddUserInfoToLog(update);
            //Начальное положение страницы для кнопок переключения страниц
            counterPage = 1;

            if (actionsCommand.containsKey(selectedCommand)) {
                final Thread commandThread = new Thread(){
                    @Override
                    public void run(){
                        SendMessage msg = actionsCommand.get(selectedCommand).handle(update);
                        commandForCallBack = selectedCommand;
                        bindingBy.put(chatId, selectedCommand);

                        if (selectedCommand.equals("/start")){
                            String path = "resources/startedImage.jpg";
                            SendPhoto msgPhoto = ConvertMessageToPhoto(msg, path);
                            send(msgPhoto);
                        } else{
                            send(msg);
                        }
                    }
                };
                commandThread.start();
                commandThread.isInterrupted();
                
            } else if (bindingBy.containsKey(chatId)) {
                final Thread removeChatIThread = new Thread(){
                    @Override
                    public void run(){
                        SendMessage msg = actionsCommand.get(bindingBy.get(chatId)).callback(update);
                        bindingBy.remove(chatId);
                        send(msg);
                    }
                };

                removeChatIThread.start();
                removeChatIThread.isInterrupted();

            } 
        //Если пришло нажатие кнопки
        } else if (update.hasCallbackQuery()){

            String selectedBtn = update.getCallbackQuery().getData();
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();

            //Если нажаты кнопки для перелистывания страниц
            if (selectedBtn.equals("backButton") || selectedBtn.equals("nextButton") || selectedBtn.equals("page")){
                //Изменяет номер страницы для кнопок отвечающих за перелистывание
                if (update.getCallbackQuery().getData().equals("backButton")) counterPage--;
                if (update.getCallbackQuery().getData().equals("nextButton")) counterPage++;

                //Получаем массив паков из любой функции бота (для функций, где нет работы с паками, возвращается null)
                MapPack pack = actionsCommand.get(commandForCallBack).getPack();
                // System.out.println("Размер пака: " + pack.SizePack());

                ScrollButtons reaction = new ScrollButtons(update, pack, counterPage);

                EditMessageText msg = reaction.GetNewMessage();
                send(msg);
            }
            
            //Если нажаты кнопки комманд
            if (actionsCommand.containsKey(selectedBtn)) {
                final Thread btnThread = new Thread(){
                    @Override
                    public void run(){
                        SendMessage msg = actionsCommand.get(selectedBtn).handle(update);
                        //msg.setReplyMarkup(MainMenuKeyboard);
                        commandForCallBack = selectedBtn;
                        bindingBy.put(chatId, selectedBtn);
                        send(msg);
                    }
                };
                btnThread.start();
                btnThread.isInterrupted();
                
            } else if (bindingBy.containsKey(chatId)) {
                final Thread removeChatIThread = new Thread(){
                    @Override
                    public void run(){
                        SendMessage msg = actionsCommand.get(bindingBy.get(chatId)).callback(update);
                        bindingBy.remove(chatId);
                        send(msg);
                    }
                };
                removeChatIThread.start();
                removeChatIThread.isInterrupted();

            }  
        }
    }
    //Метод который преобразует текстовое сообщение в сообщение с фото
    private SendPhoto ConvertMessageToPhoto(SendMessage msg, String path) {
        final SendPhoto sendPhoto = new SendPhoto();   
        InputStream stream = Main.class.getResourceAsStream(path);
        InputFile startImage = new InputFile(stream, "startedImage.jpg");

        sendPhoto.setPhoto(startImage);
        sendPhoto.setChatId(msg.getChatId());
        sendPhoto.setCaption(msg.getText());
        sendPhoto.setReplyMarkup(msg.getReplyMarkup());
        sendPhoto.setParseMode("MarkdownV2");
        return sendPhoto;
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
    //Метод отправкии в бота
    private void send(BotApiMethod mess) {
        try {
            //System.out.println(mess);
            execute(mess);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void send(SendPhoto mess) {
        try {
            //System.out.println(mess);
            execute(mess);
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
