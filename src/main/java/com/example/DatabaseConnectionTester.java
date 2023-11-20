package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnectionTester {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5433/TelegramBd";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "1234567890";

    public static void main(String[] args) {
        testDatabaseConnection();

        // Предположим, что у вас есть переменная username с новым пользователем
        String username = "@Singing_Sinking";
        addUsername(username);

        printAllUsers();
    }

    public static void testDatabaseConnection() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            e.printStackTrace();
        }
    }

    public static void addUsername(String username) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String sql = "INSERT INTO public.acc (username) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.executeUpdate();
                System.out.println("Username " + username + " added to the table.");
            } catch (SQLException e) {
                System.err.println("Failed to add username to the table.");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            e.printStackTrace();
        }
    }

    public static void printAllUsers() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String sql = "SELECT * FROM public.acc";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                java.sql.ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String existingUsername = resultSet.getString("username");
                    System.out.println("Existing Username: " + existingUsername);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            e.printStackTrace();
        }
    }
}