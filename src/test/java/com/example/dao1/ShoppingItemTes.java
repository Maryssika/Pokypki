package com.example.dao1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingItemTest {
    @Test
    void testGettersAndSetters() {
        ShoppingItem item = new ShoppingItem(1, "Milk", 2, "Dairy");

        assertEquals(1, item.getId());
        assertEquals("Milk", item.getName());
        assertEquals(2, item.getQuantity());
        assertEquals("Dairy", item.getCategory());

        item.setId(100);
        assertEquals(100, item.getId());
    }
}