package com.example.dao1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDatabaseSetup {
    public static Connection initializeTestDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS shopping_items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "category VARCHAR(100) NOT NULL)");

            // Очищаем таблицу перед тестами
            stmt.execute("DELETE FROM shopping_items");
        }

        return connection;
    }
}