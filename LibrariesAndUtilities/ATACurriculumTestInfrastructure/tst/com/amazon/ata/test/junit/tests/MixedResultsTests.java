package com.amazon.ata.test.junit.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("MIXED")
public class MixedResultsTests {

    @Test
    public void failedTest() {
        assertTrue(false);
    }

    @Test
    public void passedTest() {
        assertTrue(true);
    }
}
