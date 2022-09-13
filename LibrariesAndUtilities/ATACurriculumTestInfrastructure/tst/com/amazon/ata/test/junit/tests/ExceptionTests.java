package com.amazon.ata.test.junit.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("EXCEPTION")
public class ExceptionTests {
    @Test
    public void throwsException() {
        throw new RuntimeException("message");
    }
}
