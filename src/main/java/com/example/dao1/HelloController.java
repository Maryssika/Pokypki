package com.example.dao1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;
import java.util.List;

public class HelloController {
    @FXML
    private TextField itemNameField;

    @FXML
    private TextField itemQuantityField;

    @FXML
    private TextField itemCategoryField;

//    @FXML
//    private TextField yearField;

    @FXML
    private TableView<ShoppingItem> shoppingTable;

    @FXML
    private TableColumn<ShoppingItem, Integer> idColumn;

    @FXML
    private TableColumn<ShoppingItem, String> nameColumn;

    @FXML
    private TableColumn<ShoppingItem, Integer> quantityColumn;

    @FXML
    private TableColumn<ShoppingItem, String> categoryColumn;

    @FXML
    private ComboBox<String> databaseComboBox;

    @FXML
    private ComboBox<String> categoryFilterComboBox;

    private ObservableList<ShoppingItem> shoppingList = FXCollections.observableArrayList();
    private ShoppingListDAO shoppingListDAO = new ShoppingListDAOImpl();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Инициализация выпадающего списка для выбора базы данных
        databaseComboBox.getItems().addAll("PostgreSQL", "H2");
        databaseComboBox.setValue("PostgreSQL");

        // Обработка выбора базы данных
        databaseComboBox.setOnAction(event -> {
            String selectedDatabase = databaseComboBox.getValue().toLowerCase();
            DatabaseInitializer.setCurrentDatabase(selectedDatabase);
            refreshShoppingList();
        });

        refreshShoppingList();
        updateCategoryFilter();
    }

   //метод для отмены фильтра
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

            // Проверка заполнения обязательных полей
            if (name.isEmpty() || category.isEmpty() || quantityText.isEmpty()) {
                showAlert("Ошибка", "Все поля должны быть заполнены");
                return;
            }

            // Проверка корректности числового значения
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

            // Создаем новый товар (ID будет установлен базой данных)
            ShoppingItem newItem = new ShoppingItem(0, name, quantity, category);

            // Добавляем товар в базу данных
            shoppingListDAO.addItem(newItem);

            // Обновляем список товаров
            refreshShoppingList();

            // Очищаем поля ввода
            clearFields();

            // Обновляем фильтр категорий
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
            shoppingListDAO.deleteItem(selectedItem.getId());
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

            if (name.isEmpty()) {
                name = selectedItem.getName();
            }
            if (category.isEmpty()) {
                category = selectedItem.getCategory();
            }

            ShoppingItem updatedItem = new ShoppingItem(selectedItem.getId(), name, quantity, category);
            shoppingListDAO.updateItem(updatedItem);
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
            shoppingList.setAll(shoppingListDAO.getItemsByCategory(selectedCategory));
        } else {
            refreshShoppingList();
        }
    }

    private void refreshShoppingList() {
        shoppingList.setAll(shoppingListDAO.getAllItems());
        shoppingTable.setItems(shoppingList);
    }

    private void updateCategoryFilter() {
        List<String> categories = shoppingListDAO.getAllItems().stream()
                .map(ShoppingItem::getCategory)
                .distinct()
                .toList();
        categoryFilterComboBox.getItems().setAll(categories);
    }

    private void clearFields() {
        itemNameField.clear();
        itemQuantityField.clear();
        itemCategoryField.clear();
//        yearField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
//    @FXML
//    private void handleCheckYear() {
//        String yearText = yearField.getText();
//
//        if (yearText.isEmpty()) {
//            showAlert("Ошибка", "Пожалуйста, введите год");
//            return;
//        }
//
//        try {
//            int year = Integer.parseInt(yearText);
//            boolean isLeap = ShoppingItem.isLeapYear(year);
//
//            Alert alert = new Alert(isLeap ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING);
//            alert.setTitle("Результат проверки");
//            alert.setHeaderText(null);
//            alert.setContentText(year + (isLeap ? " - високосный год" : " - не високосный год"));
//            alert.showAndWait();
//
//        } catch (NumberFormatException e) {
//            showAlert("Ошибка", "Год должен быть целым числом");
//        }
//    }
}