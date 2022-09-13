package com.amazon.ata.test.reflect;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * Runtime exception thrown when using {@code ConstructorQuery} to query for constructors but
 * more than one constructor matched the criteria when requesting a single constructor match.
 */
public class MultipleConstructorsFoundException extends ConstructorQueryException {
    private static final long serialVersionUID = 6090518968834680354L;
    /**
     * Creates a new {@code MultipleConstructorsFoundException}.
     * @param constructorQuery the {@code ConstructorQuery} that ran into trouble.
     * @param constructors the multiple constructors matching the constructor query
     */
    public MultipleConstructorsFoundException(
        ConstructorQuery constructorQuery,
        Set<Constructor<?>> constructors) {

        super(
            constructorQuery,
            String.format(
                "Multiple constructors in type %s (%s) matched criteria: %s",
                constructorQuery.getContainingClass(),
                constructors,
                constructorQuery)
        );
    }
}
