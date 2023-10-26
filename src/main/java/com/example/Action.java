package com.example;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Action {
    BotApiMethod handle(Update update);

    BotApiMethod callback(Update update);
}
