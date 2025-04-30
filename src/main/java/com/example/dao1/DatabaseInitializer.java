package com.example.dao1;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final Dotenv dotenv = Dotenv.load();

    public static void initializeAllDatabases() {
        initializePostgreSQL();
        initializeH2();
    }

    private static void initializePostgreSQL() {
        String url = dotenv.get("POSTGRES_URL");
        String user = dotenv.get("POSTGRES_USER");
        String password = dotenv.get("POSTGRES_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS shopping_items (" +
                    "id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, " +
                    "quantity INTEGER NOT NULL, category VARCHAR(100) NOT NULL);";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("PostgreSQL error: " + e.getMessage());
        }
    }

    private static void initializeH2() {
        String url = dotenv.get("H2_URL");
        String user = dotenv.get("H2_USER");
        String password = dotenv.get("H2_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS shopping_items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, " +
                    "quantity INTEGER NOT NULL, category VARCHAR(100) NOT NULL);";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("H2 error: " + e.getMessage());
        }
    }
}