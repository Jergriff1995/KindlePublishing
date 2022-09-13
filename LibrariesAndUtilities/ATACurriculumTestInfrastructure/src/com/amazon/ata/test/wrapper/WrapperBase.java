package com.amazon.ata.test.wrapper;

import com.amazon.ata.test.reflect.ConstructorQuery;
import com.amazon.ata.test.reflect.MethodQuery;

import com.google.common.collect.Lists;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.amazon.ata.test.helper.AtaTestHelper.failTestWithException;

/**
 * Superclass for the wrapper classes that allows us to pretend we have
 * direct access to instances of the base project classes.
 *
 * Note: It may be worth exploring java.lang.reflect.Proxy, java.lang.reflect.InvocationHandler,
 * as there might be a clever way to create interfaces (that participant classes
 * do not actually implement) that we create simple handlers that wrap
 * participant class instances that the handlers actually delegate to
 * to satisfy the interface methods. We'd need to dynamically find the appropriate
 * method to call on the participant class instance, either by name/type if we can
 * force any of these....
 */
public abstract class WrapperBase {
    // the object that is an instance of the base package
    private Object wrappedInstance;

    /**
     * Saves the wrapped class instance and type, ensuring the wrapped instance looks like the right type for
     * the given wrapper class.
     *
     * @param wrappedInstance the instance to be wrapped
     */
    protected WrapperBase(final Object wrappedInstance) {
        if (wrappedInstance != null && !getWrappedClass().isInstance(wrappedInstance)) {
            throw new IllegalArgumentException(
                String.format("Unexpected wrapped instance type for %s. Expected instance to be a %s, but is a %s",
                    this.getClass().getSimpleName(),
                    getWrappedClass().getSimpleName(),
                    wrappedInstance.getClass().getSimpleName())
            );
        }

        this.wrappedInstance = wrappedInstance;
    }

    /**
     * Returns this wrapper class's wrapped class.
     *
     * Used by logic in this superclass, so we need an instance method for
     * abstract/overriding/polymorphism purposes.
     *
     * Subclasses likely implement a static version of this method, which
     * this method calls. That is an expected pattern.
     *
     * @return The class wrapped by this wrapper class
     */
    public abstract Class<?> getWrappedClass();

    @Override
    public boolean equals(final Object other) {
        if (isNull()) {
            throw new NullPointerException(
                String.format("Called equals() on a %s with null wrapped instance", this.getClass().getSimpleName())
            );
        }

        if (! (other instanceof WrapperBase)) {
            return false;
        }

        WrapperBase otherWrapper = (WrapperBase) other;

        return wrappedInstance.equals(otherWrapper.getWrappedInstance());
    }

    @Override
    public int hashCode() {
        return wrappedInstance.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s containing: %s", getWrappedClass().getSimpleName(), wrappedInstance.toString());
    }

    /**
     * Returns whether the wrapped object is null.
     *
     * @return true if object is null; false if not
     */
    public boolean isNull() {
        return wrappedInstance == null;
    }

    /**
     * Returns the object wrapped by this BaseWrapper object.
     *
     * @return The wrapped instance, that is of type returned by {@code getWrappedClass}
     */
    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    /**
     * Returns the constructor specified by {@code parameterTypes} for the class indicated by {@code wrappedClass}.
     *
     * If a matching constructor is not found, will {@code fail()}.
     *
     * @param wrappedClass The class to find a constructor for
     * @param parameterTypes The list of parameter types for the requested constructor
     * @param <T> The type to fetch the constructor for
     * @return The Constructor object, if it exists.
     */
    protected static <T> Constructor<T> getConstructor(final Class<T> wrappedClass, Class<?>... parameterTypes) {
        return ConstructorQuery.inClass(wrappedClass)
            .withExactArgTypes(Lists.newArrayList(parameterTypes))
            .findConstructorOrFail();
    }

    /**
     * Returns the Method object corresponding to the method name provided.
     *
     * Will {@code fail()} if method not found, or if more than one method with that
     * name is found.
     *
     * @param methodName The name of the method requested
     * @return the Method object corresponding to {@code methodName}
     */
    protected Method getMethod(final String methodName) {
        return MethodQuery.inType(getWrappedClass()).withExactName(methodName).findMethodOrFail();
    }

    /**
     * Invokes the given method with the provided arguments and handles the reflection exceptions.
     * @param method The method to invoke
     * @param args   The arguments to provide to the method
     */
    public void invokeVoidInstanceMethod(final Method method, Object... args) {
        WrapperBase.invokeInstanceMethodWithReturnValue(wrappedInstance, method, args);
    }

    /**
     * Invokes the given method with the provided arguments and handles the reflection exceptions.
     *
     * @param method The method to invoke
     * @param args   The arguments to provide to the method
     * @return The return value of the method, as an {@code Object}
     */
    public Object invokeInstanceMethodWithReturnValue(final Method method, Object... args) {
        return WrapperBase.invokeInstanceMethodWithReturnValue(wrappedInstance, method, args);
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
    protected static Object invokeInstanceMethodWithReturnValue(
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
    protected static Object invokeStaticMethodWithReturnValue(final Method method, Object... args) {
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
}
