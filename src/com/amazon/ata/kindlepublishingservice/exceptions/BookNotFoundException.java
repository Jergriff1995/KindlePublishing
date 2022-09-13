package com.amazon.ata.kindlepublishingservice.exceptions;

/**
 * Exception to be thrown when a book cannot be found in the catalog.
 */
public class BookNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7602617011482324248L;

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public BookNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
