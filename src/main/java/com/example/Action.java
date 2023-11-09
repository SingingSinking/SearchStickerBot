package com.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Action {
    //Первое сообщение
    SendMessage handle(Update update);
    //ответ на первое сообщение
    SendMessage callback(Update update);
    //Если нужно передать массив со стикерами (Если не нужно, null)
    MapPack getPack();
}
