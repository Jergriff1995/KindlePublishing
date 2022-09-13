package com.amazon.ata.test.reflect;

/**
 * Super-exception for exceptions related to {@code ConstructorQuery}.
 *
 * Must specify the {@code ConstructorQuery} that caused the trouble, as well as a message
 * representing the error. Can optionally provide causing {@code Throwable} as well.
 */
public abstract class ConstructorQueryException extends RuntimeException {
    private static final long serialVersionUID = -4546234507633373579L;
    private final ConstructorQuery constructorQuery;

    /**
     * Creates a new {@code ConstructorQueryException}.
     * @param constructorQuery the {@code ConstructorQuery} that ran into trouble.
     * @param message the detail message
     */
    public ConstructorQueryException(ConstructorQuery constructorQuery, String message) {
        super(message);
        this.constructorQuery = constructorQuery;
    }

    /**
     * Creates a new {@code ConstructorQueryException}.
     * @param constructorQuery the {@code ConstructorQuery} that ran into trouble.
     * @param message the detail message
     * @param cause the cause
     */
    public ConstructorQueryException(ConstructorQuery constructorQuery, String message, Throwable cause) {
        super(message, cause);
        this.constructorQuery = constructorQuery;
    }
}
