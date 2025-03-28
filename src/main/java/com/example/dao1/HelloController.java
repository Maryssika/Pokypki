package com.example.dao1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;
import java.util.List;

public class HelloController {
    @FXML private TextField itemNameField;
    @FXML private TextField itemQuantityField;
    @FXML private TextField itemCategoryField;
    @FXML private TableView<ShoppingItem> shoppingTable;
    @FXML private TableColumn<ShoppingItem, Integer> idColumn;
    @FXML private TableColumn<ShoppingItem, String> nameColumn;
    @FXML private TableColumn<ShoppingItem, Integer> quantityColumn;
    @FXML private TableColumn<ShoppingItem, String> categoryColumn;
    @FXML private ComboBox<String> databaseComboBox;
    @FXML private ComboBox<String> categoryFilterComboBox;

    private ObservableList<ShoppingItem> shoppingList = FXCollections.observableArrayList();
    private DatabaseManager databaseManager = new DatabaseManager();

    @FXML
    public void initialize() {
        // Инициализация таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Инициализация выпадающего списка для выбора базы данных
        databaseComboBox.getItems().addAll("PostgreSQL", "H2");
        databaseComboBox.setValue("PostgreSQL");

        // Обработка выбора базы данных
        databaseComboBox.setOnAction(event -> refreshShoppingList());

        // Автозаполнение полей при выборе элемента в таблице
        shoppingTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelectedItem(newSelection);
            }
        });

        refreshShoppingList();
        updateCategoryFilter();
    }

    private void fillFieldsFromSelectedItem(ShoppingItem item) {
        itemNameField.setText(item.getName());
        itemQuantityField.setText(String.valueOf(item.getQuantity()));
        itemCategoryField.setText(item.getCategory());
    }

    @FXML
    private void handleClearFilter() {
        categoryFilterComboBox.getSelectionModel().clearSelection();
        refreshShoppingList();
    }

    @FXML
    private void handleAddItem() {
        try {
            String name = itemNameField.getText();
            String quantityText = itemQuantityField.getText();
            String category = itemCategoryField.getText();

            if (name.isEmpty() || category.isEmpty() || quantityText.isEmpty()) {
                showAlert("Ошибка", "Все поля должны быть заполнены");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityText);
                if (quantity <= 0) {
                    showAlert("Ошибка", "Количество должно быть положительным числом");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Ошибка", "Количество должно быть числом");
                return;
            }

            ShoppingItem newItem = new ShoppingItem(0, name, quantity, category);
            databaseManager.addItemToAllDatabases(newItem);
            refreshShoppingList();
            clearFields();
            updateCategoryFilter();

        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось добавить товар: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteItem() {
        ShoppingItem selectedItem = shoppingTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            databaseManager.deleteItemFromAllDatabases(selectedItem.getId());
            refreshShoppingList();
            updateCategoryFilter();
        } else {
            showAlert("Ошибка", "Продукт не выбран.");
        }
    }

    @FXML
    private void handleUpdateItem() {
        ShoppingItem selectedItem = shoppingTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String name = itemNameField.getText();
            String quantityText = itemQuantityField.getText();
            String category = itemCategoryField.getText();

            if (name.isEmpty() && quantityText.isEmpty() && category.isEmpty()) {
                showAlert("Ошибка", "Необходимо изменить хотя бы один параметр.");
                return;
            }

            int quantity = selectedItem.getQuantity();
            if (!quantityText.isEmpty()) {
                try {
                    quantity = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Количество должно быть числом.");
                    return;
                }
            }

            if (name.isEmpty()) name = selectedItem.getName();
            if (category.isEmpty()) category = selectedItem.getCategory();

            ShoppingItem updatedItem = new ShoppingItem(selectedItem.getId(), name, quantity, category);
            databaseManager.updateItemInAllDatabases(updatedItem);
            refreshShoppingList();
            clearFields();
            updateCategoryFilter();
        } else {
            showAlert("Ошибка", "Продукт не выбран.");
        }
    }

    @FXML
    private void handleSortByCategory() {
        shoppingList.sort(Comparator.comparing(ShoppingItem::getCategory));
        shoppingTable.setItems(shoppingList);
    }

    @FXML
    private void handleFilterByCategory() {
        String selectedCategory = categoryFilterComboBox.getValue();
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            String currentDatabase = databaseComboBox.getValue().toLowerCase();
            shoppingList.setAll(databaseManager.getDAO(currentDatabase).getItemsByCategory(selectedCategory));
        } else {
            refreshShoppingList();
        }
    }

    private void refreshShoppingList() {
        String currentDatabase = databaseComboBox.getValue().toLowerCase();
        shoppingList.setAll(databaseManager.getAllItemsFromCurrentDatabase(currentDatabase));
        shoppingTable.setItems(shoppingList);
    }

    private void updateCategoryFilter() {
        String currentDatabase = databaseComboBox.getValue().toLowerCase();
        List<String> categories = databaseManager.getAllItemsFromCurrentDatabase(currentDatabase).stream()
                .map(ShoppingItem::getCategory)
                .distinct()
                .toList();
        categoryFilterComboBox.getItems().setAll(categories);
    }

    private void clearFields() {
        itemNameField.clear();
        itemQuantityField.clear();
        itemCategoryField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}