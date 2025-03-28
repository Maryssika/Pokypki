package com.example.dao1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeAllDatabases() {
        initializePostgreSQL();
        initializeH2();
    }

    private static void initializePostgreSQL() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "1234567";

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
        String url = "jdbc:h2:tcp://localhost/~/test";
        String user = "sa";
        String password = "1234567";

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