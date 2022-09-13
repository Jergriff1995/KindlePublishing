package com.amazon.ata.test.reflect;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Runtime exception thrown when using {@code MethodQuery} to query for methods
 * but more than one method matched the criteria when requesting a single method match.
 */
public class MultipleMethodsFoundException extends MethodQueryException {
    private static final long serialVersionUID = 8671739746425650986L;

    /**
     * Creates a new {@code MultipleMethodsFoundException}.
     * @param methodQuery the {@code MethodQuery} that ran into trouble.
     * @param methods the multiple methods matching the method query
     */
    public MultipleMethodsFoundException(final MethodQuery methodQuery, final Set<Method> methods) {
        super(
            methodQuery,
            String.format(
                "Multiple methods in type %s (%s) matching criteria: %s",
                methodQuery.getContainingType(),
                getMethodNames(methods), methodQuery.toString())
        );
    }

    private static String getMethodNames(final Set<Method> methods) {
        return methods.stream()
            .map(Method::getName)
            .collect(Collectors.joining(", "));
    }
}
