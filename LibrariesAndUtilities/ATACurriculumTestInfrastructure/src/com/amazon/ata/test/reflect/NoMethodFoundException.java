package com.amazon.ata.test.reflect;

/**
 * Runtime exception thrown when using {@code MethodQuery} to query for methods
 * but no methods matched the criteria when requesting a single method match.
 */
public class NoMethodFoundException extends MethodQueryException {
    private static final long serialVersionUID = 4506258500059729075L;

    /**
     * Creates a new {@code NoMethodFoundException}.
     * @param methodQuery the {@code MethodQuery} that ran into trouble.
     */
    public NoMethodFoundException(final MethodQuery methodQuery) {
        super(
            methodQuery,
            String.format(
                "No method found in %s matching criteria: %s",
                methodQuery.getContainingType(),
                methodQuery.toString())
        );
    }
}
