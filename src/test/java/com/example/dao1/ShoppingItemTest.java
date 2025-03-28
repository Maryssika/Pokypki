package com.example.dao1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingItemTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void isLeapYear() {
        assertTrue(ShoppingItem.isLeapYear(2004));
        assertTrue(ShoppingItem.isLeapYear(2020));
        assertTrue(ShoppingItem.isLeapYear(1996));

        // Особый случай - делится на 400
        assertTrue(ShoppingItem.isLeapYear(2000));
        assertTrue(ShoppingItem.isLeapYear(1600));

        // Не високосные года (не делятся на 4)
        assertFalse(ShoppingItem.isLeapYear(2001));
        assertFalse(ShoppingItem.isLeapYear(2019));
        assertFalse(ShoppingItem.isLeapYear(1900)); // Делится на 100, но не на 400

        // Граничные случаи
        assertTrue(ShoppingItem.isLeapYear(0));  // Год 0 считается високосным в григорианском календаре
        assertFalse(ShoppingItem.isLeapYear(1));
        assertTrue(ShoppingItem.isLeapYear(4));  // Первый високосный год

        // Отрицательные года (до н.э.)
        assertTrue(ShoppingItem.isLeapYear(-4));
        assertFalse(ShoppingItem.isLeapYear(-1));
        assertTrue(ShoppingItem.isLeapYear(-400));
    }
}