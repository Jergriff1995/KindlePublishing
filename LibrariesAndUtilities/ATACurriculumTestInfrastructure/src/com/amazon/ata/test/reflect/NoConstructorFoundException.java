package com.amazon.ata.test.reflect;

/**
 * Runtime exception thrown when using {@code ConstructorQuery} to query for constructors but
 * no constructors matched the criteria when requesting a single constructor match.
 */
public class NoConstructorFoundException extends ConstructorQueryException {
    private static final long serialVersionUID = 838012410362975149L;
    /**
     * Creates a new {@code NoConstructorFoundException}.
     * @param constructorQuery the {@code ConstructorQuery} that ran into trouble.
     */
    public NoConstructorFoundException(ConstructorQuery constructorQuery) {
        super(
            constructorQuery,
            String.format(
                "No constructor found in %s matching criteria: %s",
                constructorQuery.getContainingClass(),
                constructorQuery)
        );
    }
}
