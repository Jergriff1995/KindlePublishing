package com.amazon.ata.test.reflect;

/**
 * Super-exception for exceptions related to {@code MethodQuery}.
 *
 * Must specify the {@code MethodQuery} that caused the trouble, as well as a
 * message representing the error. Can optionally provide causing {@code Throwable}
 * as well.
 */
public abstract class MethodQueryException extends RuntimeException {
    private static final long serialVersionUID = 3299975974572902158L;
    private final MethodQuery methodQuery;

    /**
     * Creates a new {@code MethodQueryException}.
     * @param methodQuery the {@code MethodQuery} that ran into trouble.
     * @param message the detail message
     */
    public MethodQueryException(final MethodQuery methodQuery, final String message) {
        super(message);
        this.methodQuery = methodQuery;
    }

    /**
     * Creates a new {@code MethodQueryException}.
     * @param methodQuery the {@code MethodQuery} that ran into trouble.
     * @param message the detail message
     * @param cause the cause
     */
    public MethodQueryException(final MethodQuery methodQuery, final String message, final Throwable cause) {
        super(message, cause);
        this.methodQuery = methodQuery;
    }
}
