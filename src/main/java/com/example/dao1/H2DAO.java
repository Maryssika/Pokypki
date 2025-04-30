package com.example.dao1;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2DAO implements ShoppingListDAO {
    private static final Dotenv dotenv = Dotenv.load();
    private final String url = dotenv.get("POSTGRES_URL");
    private final String user = dotenv.get("POSTGRES_USER");
    private final String password = dotenv.get("POSTGRES_PASSWORD");

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public List<ShoppingItem> getAllItems() {
        List<ShoppingItem> items = new ArrayList<>();
        String sql = "SELECT id, name, quantity, category FROM shopping_items";

        try (Connection conn = connect();
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
            System.out.println("H2 getAllItems error: " + e.getMessage());
        }
        return items;
    }

    @Override
    public List<ShoppingItem> getItemsByCategory(String category) {
        List<ShoppingItem> items = new ArrayList<>();
        String sql = "SELECT id, name, quantity, category FROM shopping_items WHERE category = ?";

        try (Connection conn = connect();
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
            System.out.println("H2 getItemsByCategory error: " + e.getMessage());
        }
        return items;
    }

    @Override
    public ShoppingItem getItemById(int id) {
        String sql = "SELECT id, name, quantity, category FROM shopping_items WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new ShoppingItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("category"));
            }
        } catch (SQLException e) {
            System.out.println("H2 getItemById error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void addItem(ShoppingItem item) {
        String sql = "INSERT INTO shopping_items(name, quantity, category) VALUES(?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getQuantity());
            pstmt.setString(3, item.getCategory());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("H2 addItem error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(ShoppingItem item) {
        String sql = "UPDATE shopping_items SET name = ?, quantity = ?, category = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getQuantity());
            pstmt.setString(3, item.getCategory());
            pstmt.setInt(4, item.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("H2 updateItem error: " + e.getMessage());
        }
    }

    @Override
    public void deleteItem(int id) {
        String sql = "DELETE FROM shopping_items WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("H2 deleteItem error: " + e.getMessage());
        }
    }
}