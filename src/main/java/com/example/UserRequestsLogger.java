package com.example;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.Date;
import java.text.SimpleDateFormat;

public class UserRequestsLogger {
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private SimpleDateFormat dateFormat;

    public UserRequestsLogger(String logFilePath) {
        try {
            fileWriter = new FileWriter(logFilePath, true); // Открываем файл для записи (true для добавления в конец файла)
            bufferedWriter = new BufferedWriter(fileWriter);
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Формат даты и времени
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для записи запроса пользователя в лог-файл
    public void logUserRequest(String request) {
        Date now = new Date();
        String formattedDate = dateFormat.format(now);

        try {
            bufferedWriter.write(formattedDate + " - " + request); // Записываем запрос с датой и временем
            bufferedWriter.newLine(); // Переходим на новую строку для следующего запроса
            bufferedWriter.flush(); // Принудительно записываем данные в файл
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для закрытия лог-файла при завершении работы
    public void close() {
        try {
            bufferedWriter.close(); // Закрываем буфер
            fileWriter.close(); // Закрываем файл
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для записи запроса пользователя с указанием его имени (username) в лог-файл
    public void logUserRequest(String username, String request) {
        Date now = new Date();
        String formattedDate = dateFormat.format(now);
    
        try {
            bufferedWriter.write(formattedDate + " - " + "User: @" + username + " - " + request); // Записываем запрос с датой, временем и именем пользователя
            bufferedWriter.newLine(); // Переходим на новую строку для следующего запроса
            bufferedWriter.flush(); // Принудительно записываем данные в файл
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



