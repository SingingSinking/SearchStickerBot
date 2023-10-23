package com.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.Set;
import java.util.HashSet;


public class GreetingBot extends TelegramLongPollingBot {

    public String nameSearchPack = "";
    private Set<Long> chatIds = new HashSet<>();
    private UserRequestsLogger requestsLogger;
    
    final int MessageLimit = 20; // Константа ограничивающая количествво отправляемых стикеров

    // Конструктор по умолчанию, использует файл "user_requests.log" для записи запросов
    public GreetingBot() {
        this("user_requests.log");
        
    }

    // Конструктор, позволяющий указать путь к файлу лога запросов
    public GreetingBot(String logFilePath) {
        requestsLogger = new UserRequestsLogger(logFilePath);
    }

    //перезаписывает метод который возникает при действии со стороны пользователя
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            String username = message.getFrom().getUserName(); // Получение username пользователя
            String firstName = message.getFrom().getFirstName();
            String lastName = message.getFrom().getLastName();
            String userLangCode = message.getFrom().getLanguageCode();
            String userRequest = message.getText();
            requestsLogger.logUserRequest(username, firstName, lastName, userLangCode, userRequest); // Запись пользователя в лог
            
            
            if (message.isCommand() && message.getText().equals("/start")) {
                sendWelcomeMessage(chatId.toString());
                
            } else {
                // Добавляем или удаляем идентификатор чата в зависимости от вашей логики
                if (message.getText().equals("/start")) {
                    chatIds.add(chatId);
                }
                nameSearchPack = message.getText();
                sendTextMessage(chatId.toString(), "Ищем стикеры по вашему запросу... ");

                
                Website combotSite = new Website("https://combot.org/telegram/stickers?q=" + nameSearchPack, "combot");
                Website chpicSite = new Website("https://chpic.su/ru/stickers/search/" + nameSearchPack + "/?searchModule=stickers", "chpic");
                
                    //Создаем паки по отдельности
                // MapPack combotPack = new MapPack(combotSite);
                // MapPack chpicPack = new MapPack(chpicSite);
                
                    //Создаем общий пак который содержит все стикеры из двух сайтов.
                    //Общий пак содержит сначала стикеры из первого, а потом из второго сайта
                MapPack allPack = new MapPack(chpicSite, combotSite);

                for (int i = 0; i < MessageLimit; i++) {
                    CheckNullPack(allPack, message);

                    String packName = allPack.GetNamePack(i);
                    String packUrl = allPack.GetUrlPack(i);
                    String imgUrl = allPack.GetUrlImgPack(i);

                    // Отправляем стикер из стикерпака
                    sendStickerFromPack(message.getChatId().toString(), imgUrl);

                    String messageText = "Имя: " + packName + "\nСсылка на скачивание: " + packUrl;
                    sendTextMessage(message.getChatId().toString(), messageText);
                }

            }
        }
    }
    
    public void close() {
        // Вызовите этот метод для закрытия файла перед завершением работы бота
        requestsLogger.close();
    }

    private void CheckNullPack(MapPack pack1, MapPack pack2, Message message){
        if (pack1.SizePack() == 0 && pack2.SizePack() == 0) {
            sendTextMessage(message.getChatId().toString(), "По вашему запросу не обнаружены стикеры :(\nВозможно, вам стоит немного изменить текст сообщения или попробовать другой язык");
        }
    }
    
    private void CheckNullPack(MapPack pack, Message message){
        if (pack.SizePack() == 0) {
            sendTextMessage(message.getChatId().toString(), "По вашему запросу не обнаружены стикеры :(\nВозможно, вам стоит немного изменить текст сообщения или попробовать другой язык");
        }
    }

    private void sendStickerFromPack(String chatId, String stickerUrl) {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(chatId);
        sendSticker.setSticker(new InputFile(stickerUrl)); // Устанавливаем URL стикера

        try {
            execute(sendSticker);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendWelcomeMessage(String chatId) {
        String welcomeText = "Привет! \nЯ бот для поиска стикеров и жду любого сообщения для начала поиска стикеров!";
        sendTextMessage(chatId, welcomeText);
    }

    private void sendTextMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "@Search_Stiker_bot";
    }

    @Override
    public String getBotToken() {
        return "6670951712:AAG7SQr3bB0soMaaZIW0xcjr6skoXlg4LS4";
    }
}
