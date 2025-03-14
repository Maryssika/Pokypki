package com.example.dao1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;

public class HelloController {
    @FXML
    private TextField itemNameField;

    @FXML
    private TextField itemQuantityField;

    @FXML
    private TextField itemCategoryField;

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

    private ObservableList<ShoppingItem> shoppingList = FXCollections.observableArrayList();
    private ShoppingListDAO shoppingListDAO = new ShoppingListDAOImpl();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        refreshShoppingList();
    }

    @FXML
    private void handleAddItem() {
        String name = itemNameField.getText();
        int quantity = Integer.parseInt(itemQuantityField.getText());
        String category = itemCategoryField.getText();
        ShoppingItem newItem = new ShoppingItem(shoppingListDAO.getAllItems().size() + 1, name, quantity, category);
        shoppingListDAO.addItem(newItem);
        refreshShoppingList();
    }

    @FXML
    private void handleDeleteItem() {
        ShoppingItem selectedItem = shoppingTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            shoppingListDAO.deleteItem(selectedItem.getId());
            refreshShoppingList();
        }
    }

    @FXML
    private void handleUpdateItem() {
        ShoppingItem selectedItem = shoppingTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String name = itemNameField.getText();
            int quantity = Integer.parseInt(itemQuantityField.getText());
            String category = itemCategoryField.getText();
            ShoppingItem updatedItem = new ShoppingItem(selectedItem.getId(), name, quantity, category);
            shoppingListDAO.updateItem(updatedItem);
            refreshShoppingList();
        }
    }

    @FXML
    private void handleSortByCategory() {
        shoppingList.sort(Comparator.comparing(ShoppingItem::getCategory));
        shoppingTable.setItems(shoppingList);
    }

    private void refreshShoppingList() {
        shoppingList.setAll(shoppingListDAO.getAllItems());
        shoppingTable.setItems(shoppingList);
    }
}