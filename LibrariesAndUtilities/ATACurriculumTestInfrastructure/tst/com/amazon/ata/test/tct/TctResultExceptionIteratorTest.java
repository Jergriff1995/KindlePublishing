package com.amazon.ata.test.tct;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TctResultExceptionIteratorTest {
    private static final String CAUSE_ERROR_MESSAGE =
            "The cause of the RuntimeException should be the exception passed to the constructor.";

    @Test
    public void hasNext_givenFileNotFoundException_throwsRuntimeException() {
        // GIVEN
        TctResultExceptionIterator iterator = new TctResultExceptionIterator(new FileNotFoundException());

        // WHEN
        try {
            iterator.hasNext();
            fail("hasNext() should have thrown a RuntimeException.");
        } catch (Exception e) {
            // THEN
            assertTrue(e instanceof RuntimeException, "hasNext() should throw a RuntimeException");
            assertTrue(e.getCause() instanceof FileNotFoundException,CAUSE_ERROR_MESSAGE);
        }
    }

    @Test
    public void next_givenFileNotFoundException_throwsRuntimeException() {
        // GIVEN
        TctResultExceptionIterator iterator = new TctResultExceptionIterator(new FileNotFoundException());

        // WHEN
        try {
            iterator.next();
            fail("next() should have thrown a RuntimeException.");
        } catch (Exception e) {
            // THEN
            assertTrue(e instanceof RuntimeException, "next() should throw a RuntimeException");
            assertTrue(e.getCause() instanceof FileNotFoundException, CAUSE_ERROR_MESSAGE);
        }
    }

    @Test
    public void remove_givenFileNotFoundException_throwsRuntimeException() {
        // GIVEN
        TctResultExceptionIterator iterator = new TctResultExceptionIterator(new FileNotFoundException());

        // WHEN
        try {
            iterator.remove();
            fail("remove() should have thrown a RuntimeException.");
        } catch (Exception e) {
            // THEN
            assertTrue(e instanceof RuntimeException, "remove() should throw a RuntimeException");
            assertTrue(e.getCause() instanceof FileNotFoundException, CAUSE_ERROR_MESSAGE);
        }
    }
}
