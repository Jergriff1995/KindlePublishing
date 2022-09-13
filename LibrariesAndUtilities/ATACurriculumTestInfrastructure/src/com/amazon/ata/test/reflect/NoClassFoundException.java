package com.amazon.ata.test.reflect;

/**
 * Runtime exception thrown when using {@code ClassQuery} to query for classes
 * but no classes matched the criteria when requesting a single class match.
 */
public class NoClassFoundException extends ClassQueryException {
    private static final long serialVersionUID = -6772241513766359206L;

    /**
     * Creates a new {@code NoClassFoundException}.
     * @param classQuery the {@code ClassQuery} that ran into trouble.
     */
    public NoClassFoundException(final ClassQuery classQuery) {
        super(
            classQuery,
            String.format(
                "No class found under package '%s' matching criteria: %s",
                classQuery.getPackageName(),
                classQuery.toString())
        );
    }
}
