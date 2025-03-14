package com.example.dao1;

import java.util.List;

public interface ShoppingListDAO {
    List<ShoppingItem> getAllItems();
    List<ShoppingItem> getItemsByCategory(String category);
    ShoppingItem getItemById(int id);
    void addItem(ShoppingItem item);
    void updateItem(ShoppingItem item);
    void deleteItem(int id);
}