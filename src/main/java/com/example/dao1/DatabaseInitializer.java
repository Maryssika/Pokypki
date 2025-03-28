package com.example.dao1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static String currentDatabase = "postgres"; // По умолчанию PostgreSQL

    public static void initializeDatabase() {
        String url;
        String user;
        String password;

        if (currentDatabase.equals("h2")) {
            url = "jdbc:h2:tcp://localhost/~/test"; // H2 в памяти
            user = "sa";
            password = "1234567";
        } else {
            url = "jdbc:postgresql://localhost:5432/postgres"; // PostgreSQL
            user = "postgres";
            password = "1234567";
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                // Создание таблицы, если она не существует
                String sql = "CREATE TABLE IF NOT EXISTS shopping_items (\n"
                        + " id SERIAL PRIMARY KEY,\n"
                        + " name VARCHAR(100) NOT NULL,\n"
                        + " quantity INTEGER NOT NULL,\n"
                        + " category VARCHAR(100) NOT NULL\n"
                        + ");";
                stmt.execute(sql);
               }
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    public static void setCurrentDatabase(String database) {
        currentDatabase = database;
        initializeDatabase(); // Переинициализация базы данных при смене
    }

    public static String getCurrentDatabase() {
        return currentDatabase;
    }
}