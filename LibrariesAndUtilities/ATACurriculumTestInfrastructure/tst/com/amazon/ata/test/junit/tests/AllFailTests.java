package com.amazon.ata.test.junit.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("FAILED")
public class AllFailTests {
    @Test
    public void failedTest1() {
        assertTrue(false);
    }

    @Test
    public void failedTest2() {
        assertTrue(false);
    }

}
