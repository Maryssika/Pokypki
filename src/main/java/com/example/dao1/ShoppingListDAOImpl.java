package com.example.dao1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListDAOImpl implements ShoppingListDAO {
    private Connection connect() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "1234567";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
        return item;
    }

    @Override
    public void addItem(ShoppingItem item) {
        String sql = "INSERT INTO shopping_items(name, quantity, category) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getQuantity());
            pstmt.setString(3, item.getCategory());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
    }
}