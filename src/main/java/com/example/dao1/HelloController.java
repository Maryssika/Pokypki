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

    @FXML
    private TextField yearField;

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

    @FXML
    private void handleAddItem() {
        try {
            String name = itemNameField.getText();
            int quantity = Integer.parseInt(itemQuantityField.getText());
            String category = itemCategoryField.getText();
            int year = Integer.parseInt(yearField.getText());


            if (name.isEmpty() || category.isEmpty()) {
                showAlert("Ошибка", "Поля 'Название' и 'Категория' не могут быть пустыми.");
                return;
            }
            if (ShoppingItem.isLeapYear(year)) {
                quantity *= 2; // Увеличиваем количество в два раза
            }

            ShoppingItem newItem = new ShoppingItem(shoppingListDAO.getAllItems().size() + 1, name, quantity, category);

            shoppingListDAO.addItem(newItem);
            refreshShoppingList();
            clearFields();
            updateCategoryFilter();
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Количество и год должны быть числами.");
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
        yearField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}