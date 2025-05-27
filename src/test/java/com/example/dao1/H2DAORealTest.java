package com.example.dao1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerRealTest {
    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() throws SQLException {
        Connection h2Connection = TestDatabaseSetup.initializeTestDatabase();
        Connection postgresConnection = TestDatabaseSetup.initializeTestDatabase(); // Для тестов используем ту же H2

        H2DAO h2DAO = new H2DAO(() -> h2Connection);
        PostgreSQLDAO postgresDAO = new PostgreSQLDAO(() -> postgresConnection);

        databaseManager = new DatabaseManager(postgresDAO, h2DAO);
    }

    @Test
    void testAddToAllDatabases() {
        ShoppingItem item = new ShoppingItem(0, "Test Item", 1, "Test");
        databaseManager.addItemToAllDatabases(item);

        List<ShoppingItem> postgresItems = databaseManager.getDAO("postgresql").getAllItems();
        List<ShoppingItem> h2Items = databaseManager.getDAO("h2").getAllItems();

        assertEquals(1, postgresItems.size());
        assertEquals(1, h2Items.size());
        assertEquals("Test Item", postgresItems.get(0).getName());
        assertEquals("Test Item", h2Items.get(0).getName());
    }

    @Test
    void testGetCurrentDatabaseItems() {
        databaseManager.getDAO("postgresql").addItem(new ShoppingItem(0, "PG Only", 1, "Test"));
        databaseManager.getDAO("h2").addItem(new ShoppingItem(0, "H2 Only", 1, "Test"));

        List<ShoppingItem> postgresItems = databaseManager.getAllItemsFromCurrentDatabase("postgresql");
        List<ShoppingItem> h2Items = databaseManager.getAllItemsFromCurrentDatabase("h2");

        assertEquals("PG Only", postgresItems.get(0).getName());
        assertEquals("H2 Only", h2Items.get(0).getName());
    }
}