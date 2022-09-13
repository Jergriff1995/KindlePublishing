package com.amazon.ata.test.reflect;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Runtime exception thrown when using {@code ClassQuery} to query for classes
 * but more than one class matched the criteria when requesting a single class match.
 */
public class MultipleClassesFoundException extends ClassQueryException {
    private static final long serialVersionUID = 657857841066656L;

    /**
     * Creates a new {@code MultipleClassesFoundException}.
     * @param classQuery the {@code ClassQuery} that ran into trouble.
     * @param classes the multiple classes matching the class query
     */
    public MultipleClassesFoundException(final ClassQuery classQuery, final Set<Class<?>> classes) {
        super(
            classQuery,
            String.format(
                "Multiple classes (%s) under package '%s' matching criteria: %s",
                getClassNames(classes),
                classQuery.getPackageName(),
                classQuery.toString())
        );
    }

    private static String getClassNames(final Set<Class<?>> classes) {
        return classes.stream()
            .map(Class::getName)
            .collect(Collectors.joining(", "));
    }
}
