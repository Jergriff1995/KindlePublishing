package com.amazon.ata.kindlepublishingservice.exceptions;

/**
 * Exception to be thrown when no publishing statuses are found for a given publishing status ID.
 */
public class PublishingStatusNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -4169834149795652806L;

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public PublishingStatusNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public PublishingStatusNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
