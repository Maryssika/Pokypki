package com.example.dao1;

import java.util.List;

public class DatabaseManager {
    private final ShoppingListDAO postgresDAO;
    private final ShoppingListDAO h2DAO;

    public DatabaseManager() {
        this.postgresDAO = new PostgreSQLDAO();
        this.h2DAO = new H2DAO();
    }

    public void addItemToAllDatabases(ShoppingItem item) {
        postgresDAO.addItem(item);
        h2DAO.addItem(item);
    }

    public void updateItemInAllDatabases(ShoppingItem item) {
        postgresDAO.updateItem(item);
        h2DAO.updateItem(item);
    }

    public void deleteItemFromAllDatabases(int id) {
        postgresDAO.deleteItem(id);
        h2DAO.deleteItem(id);
    }

    public List<ShoppingItem> getAllItemsFromCurrentDatabase(String currentDatabase) {
        return getDAO(currentDatabase).getAllItems();
    }

    public ShoppingListDAO getDAO(String database) {
        return database.equals("h2") ? h2DAO : postgresDAO;
    }

    // Альтернативный вариант вместо getItemsByCategoryFromCurrentDatabase
    public List<ShoppingItem> getItemsByCategory(String category, String currentDatabase) {
        return getDAO(currentDatabase).getItemsByCategory(category);
    }
}