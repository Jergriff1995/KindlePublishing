package com.amazon.ata.test.reflect;

/**
 * Super-exception for exceptions related to {@code ClassQuery}.
 *
 * Must specify the {@code ClassQuery} that caused the trouble, as well as a
 * message representing the error. Can optionally provide causing {@code Throwable}
 * as well.
 */
public abstract class ClassQueryException extends RuntimeException {
    private static final long serialVersionUID = 238852215427573244L;
    private final ClassQuery classQuery;

    /**
     * Creates a new {@code ClassQueryException}.
     * @param classQuery the {@code ClassQuery} that ran into trouble.
     * @param message the detail message
     */
    public ClassQueryException(final ClassQuery classQuery, final String message) {
        super(message);
        this.classQuery = classQuery;
    }

    /**
     * Creates a new {@code ClassQueryException}.
     * @param classQuery the {@code ClassQuery} that ran into trouble.
     * @param message the detail message
     * @param cause the cause
     */
    public ClassQueryException(final ClassQuery classQuery, final String message, final Throwable cause) {
        super(message, cause);
        this.classQuery = classQuery;
    }
}
