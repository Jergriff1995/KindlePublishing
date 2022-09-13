package com.amazon.ata.test.helper;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AtaTestHelperTest {

    @Test
    public void getFileContentFromResources_resourceDoesNotExist_throwsException() {
        // GIVEN
        String fileName = "unknownFile.txt";

        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> AtaTestHelper.getFileContentFromResources(fileName),
            "Expected exception to be thrown for a non-existent file.");
    }

    @Test
    public void getFileContentFromResources_resourceExists_returnsContent() {
        // GIVEN
        String fileName = AtaTestHelper.class.getName().replace(".", "/") + ".class";

        // WHEN
        String content = AtaTestHelper.getFileContentFromResources(fileName);

        // THEN
        assertFalse(content.trim().isEmpty(), "Expected known file to have contents.");
        assertTrue(content.contains("AtaTestHelper"), "Expected to contain class name.");
        assertTrue(content.contains("getFileContentFromResources"), "Expected to contain method under test.");
    }

    @Test
    void failTestWithException_assertFires() {
        // GIVEN - exception
        Exception e = new IllegalArgumentException("That's not fair!");

        // WHEN + THEN - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> AtaTestHelper.failTestWithException(e, "summary")
        );
    }
}
