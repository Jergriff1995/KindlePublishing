package com.amazon.stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockTest {
    private String id = "id";
    private String name = "name";
    private Stock stock;

    @BeforeEach
    public void setUp() {
        stock = new Stock(id, name);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        // WHEN
        boolean isEqual = stock.equals(stock);

        // THEN
        assertTrue(isEqual);
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        // WHEN
        boolean isEqual = stock.equals(null);

        // THEN
        assertFalse(isEqual);
    }

    @Test
    public void equals_differentClass_returnsFalse() {
        // GIVEN
        Integer other = 1;

        // WHEN
        boolean isEqual = stock.equals(other);

        // THEN
        assertFalse(isEqual);
    }

    @Test
    public void equals_differentIds_returnsFalse() {
        // GIVEN
        Stock other = new Stock("other", name);

        // WHEN
        boolean isEqual = stock.equals(other);

        // THEN
        assertFalse(isEqual);
    }

    @Test
    public void equals_differentNames_returnsTrue() {
        // GIVEN
        Stock other = new Stock(id, "other");

        // WHEN
        boolean isEqual = stock.equals(other);

        // THEN
        assertTrue(isEqual);
    }

    @Test
    public void equals_sameIdAndName_returnsTrue() {
        // GIVEN
        Stock other = new Stock(id, name);

        // WHEN
        boolean isEqual = stock.equals(other);

        // THEN
        assertTrue(isEqual);
    }

    @Test
    public void hashCode_sameIdDifferentName_sameHashCode() {
        // GIVEN
        Stock other = new Stock(id, "other");

        // WHEN + THEN
        assertEquals(stock.hashCode(), other.hashCode());
    }

    @Test
    public void hashCode_sameIdAndName_sameHashCode() {
        // GIVEN
        Stock other = new Stock(id, name);

        // WHEN + THEN
        assertEquals(stock.hashCode(), other.hashCode());
    }

    @Test
    public void hashCode_differentId_differentHashCode() {
        // GIVEN
        Stock other = new Stock("other", name);

        // WHEN + THEN
        assertNotEquals(stock.hashCode(), other.hashCode());
    }
}
