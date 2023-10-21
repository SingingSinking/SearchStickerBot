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
            fileWriter = new FileWriter(logFilePath, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Формат даты и времени
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logUserRequest(String request) {
        Date now = new Date();
        String formattedDate = dateFormat.format(now);

        try {
            bufferedWriter.write(formattedDate + " - " + request);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void logUserRequest(String username, String request) {
        Date now = new Date();
        String formattedDate = dateFormat.format(now);
    
        try {
            bufferedWriter.write(formattedDate + " - " + "User: @" + username + " - " + request);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


