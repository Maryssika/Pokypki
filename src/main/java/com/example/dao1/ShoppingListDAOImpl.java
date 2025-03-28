package com.example.dao1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListDAOImpl implements ShoppingListDAO {
    private Connection connect() {
        String url;
        String user;
        String password;

        if (DatabaseInitializer.getCurrentDatabase().equals("h2")) {
            url = "jdbc:h2:tcp://localhost/~/test";
            user = "sa";
            password = "1234567";
        } else {
            url = "jdbc:postgresql://localhost:5432/postgres";
            user = "postgres";
            password = "1234567";
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
        return conn;
    }

    @Override
    public List<ShoppingItem> getAllItems() {
        String sql = "SELECT id, name, quantity, category FROM shopping_items";
        List<ShoppingItem> items = new ArrayList<>();

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new ShoppingItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("category")));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении данных: " + e.getMessage());
        }
        return items;
    }

    @Override
    public List<ShoppingItem> getItemsByCategory(String category) {
        String sql = "SELECT id, name, quantity, category FROM shopping_items WHERE category = ?";
        List<ShoppingItem> items = new ArrayList<>();

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                items.add(new ShoppingItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("category")));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при фильтрации по категории: " + e.getMessage());
        }
        return items;
    }

    @Override
    public ShoppingItem getItemById(int id) {
        String sql = "SELECT id, name, quantity, category FROM shopping_items WHERE id = ?";
        ShoppingItem item = null;

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                item = new ShoppingItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("category"));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении товара по ID: " + e.getMessage());
        }
        return item;
    }

    @Override
    public void addItem(ShoppingItem item) {
        String sql = "INSERT INTO shopping_items(name, quantity, category) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getQuantity());
            pstmt.setString(3, item.getCategory());
            pstmt.executeUpdate();

            // Получаем сгенерированный ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении товара: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(ShoppingItem item) {
        String sql = "UPDATE shopping_items SET name = ?, quantity = ?, category = ? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getQuantity());
            pstmt.setString(3, item.getCategory());
            pstmt.setInt(4, item.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении товара: " + e.getMessage());
        }
    }

    @Override
    public void deleteItem(int id) {
        String sql = "DELETE FROM shopping_items WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении товара: " + e.getMessage());
        }
    }
}