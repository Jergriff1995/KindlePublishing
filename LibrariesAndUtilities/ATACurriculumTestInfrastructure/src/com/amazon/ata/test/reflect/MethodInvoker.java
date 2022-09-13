package com.amazon.ata.test.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.amazon.ata.test.helper.AtaTestHelper.failTestWithException;

public final class MethodInvoker {
    private MethodInvoker() {
    }

    /**
     * Invokes the given method with the provided arguments on the specified object, handling the reflection
     * exceptions.
     *
     * @param invokeTarget The object to invoke the given method on
     * @param method The method to invoke
     * @param args The arguments to provide to the method
     * @return The return value of the method, as an {@code Object}
     */
    public static Object invokeInstanceMethodWithReturnValue(
        final Object invokeTarget, final Method method, Object... args) {

        if (null == invokeTarget) {
            throw new NullPointerException(
                String.format("Attempted to call method %s on a null instance of %s",
                    method.getName(),
                    method.getDeclaringClass().getSimpleName())
            );
        }

        Object returnValue = null;

        try {
            returnValue = method.invoke(invokeTarget, args);
        } catch (IllegalAccessException e) {
            failTestWithException(e, String.format("Unable to access %s on %s",
                method.getName(),
                invokeTarget.getClass().getSimpleName()));
        } catch (InvocationTargetException e) {
            failTestWithException(e.getCause(), String.format("Failed to successfully call %s on %s",
                method.getName(),
                invokeTarget.getClass().getSimpleName()));
        }

        return returnValue;
    }

    /**
     * Calls the given static method with the provided arguments and handles the reflection exceptions.
     *
     * @param method The method to invoke
     * @param args The arguments to provide to the method
     * @return The return value of the method, as an {@code Object}
     */
    public static Object invokeStaticMethodWithReturnValue(final Method method, Object... args) {
        Object returnValue = null;

        try {
            // for static methods, pass null for the instance method is invoked on
            returnValue = method.invoke(null, args);
        } catch (IllegalAccessException e) {
            failTestWithException(e, String.format("Unable to access %s on %s",
                method.getName(),
                method.getDeclaringClass().getSimpleName()));
        } catch (InvocationTargetException e) {
            failTestWithException(e.getCause(), String.format("Failed to successfully call %s on %s",
                method.getName(),
                method.getDeclaringClass().getSimpleName()));
        }

        return returnValue;
    }

    /**
     * Calls the provided constructor with the given arguments and does exception handling,
     * assertion failure reporting.
     *
     * @param constructor The constructor to call
     * @param parameters The args to pass to the constructor
     * @param <T> The (wrapped class) type the constructor belongs to
     * @return The constructed instance
     */
    public static <T> T invokeConstructor(final Constructor<T> constructor, Object... parameters) {
        T newInstance = null;

        try {
            newInstance = constructor.newInstance(sortParametersByType(constructor.getParameterTypes(), parameters));
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            List<String> argList = new ArrayList<>();
            for (Object arg : parameters) {
                argList.add(arg.toString());
            }

            failTestWithException(
                e,
                String.format("Failed to instantiate a new %s with args (%s)",
                    constructor.getDeclaringClass().getSimpleName(),
                    String.join(", ", argList))
            );
        }

        return newInstance;
    }

    private static Object[] sortParametersByType(Type[] declaredParameters, Object... parameters) {
        Set<Type> parameterSet = new HashSet<>(Arrays.asList(declaredParameters));
        Object[] sortedParameters = new Object[parameters.length];

        // first check if the multiple declared params are unique.
        // no-op if there is a single param or if there are any repeated types
        // and assume the order is intentional
        if (declaredParameters.length > 1 && parameterSet.size() == declaredParameters.length) {
            for (int i = 0; i < parameters.length; i++) {
                // search through the declared parameters for the current parameter
                for (int j = 0; j < declaredParameters.length; j++) {
                    if (((Class) declaredParameters[j]).isAssignableFrom(parameters[i].getClass())) {
                        sortedParameters[j] = parameters[i];
                    }
                }
            }
            return sortedParameters;
        }

        return parameters;
    }
}
