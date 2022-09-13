package com.amazon.ata.test.junit.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("SETUP_FAILS")
public class SetupFailTest {
    @BeforeAll
    public static void setup() {
        fail();
    }

    @Test
    public void passedTest1() {
        assertTrue(true);
    }
}
