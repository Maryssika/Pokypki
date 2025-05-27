package com.example.dao1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

interface ShoppingListDAOTest<T extends ShoppingListDAO> {
    T createDAO();

    @Test
    default void testAddAndGetItem() {
        ShoppingListDAO dao = createDAO();
        ShoppingItem item = new ShoppingItem(0, "Test", 5, "Category");

        dao.addItem(item);
        ShoppingItem retrieved = dao.getItemById(item.getId());

        assertNotNull(retrieved);
        assertEquals("Test", retrieved.getName());
    }
}

// Реализация для PostgreSQL
class PostgreSQLDAOInterfaceTest implements ShoppingListDAOTest<PostgreSQLDAO> {
    @Override
    public PostgreSQLDAO createDAO() {
        return new PostgreSQLDAO();
    }
}

// Реализация для H2
class H2DAOInterfaceTest implements ShoppingListDAOTest<H2DAO> {
    @Override
    public H2DAO createDAO() {
        return new H2DAO();
    }
}