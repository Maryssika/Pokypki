package com.example.dao1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "1234567";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS shopping_items (\n"
                        + " id SERIAL PRIMARY KEY,\n"
                        + " name VARCHAR(100) NOT NULL,\n"
                        + " quantity INTEGER NOT NULL,\n"
                        + " category VARCHAR(100) NOT NULL\n"
                        + ");";
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
