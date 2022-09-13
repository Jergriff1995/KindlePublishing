package com.amazon.ata.test.junit.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllPassTests {

    @Tag("PASSED")
    @Test
    public void passedTest1() {
        assertTrue(true);
    }

    @Tag("PASSED")
    @Test
    public void passedTest2() {
        assertTrue(true);
    }
}
