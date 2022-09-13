package com.amazon.ata.test.wrapper;

import com.amazon.ata.test.reflect.MethodQuery;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WrapperBaseTest {

    public static class StringWrapper extends WrapperBase {
        StringWrapper(Object wrappedInstance) {
            super(wrappedInstance);
        }

        public Class<?> getWrappedClass() {
            return String.class;
        }
    }

    public static class IntegerWrapper extends WrapperBase {
        IntegerWrapper(Object wrappedInstance) {
            super(wrappedInstance);
        }

        public Class<?> getWrappedClass() {
            return Integer.class;
        }
    }

    public static class ClassWithVoidMethods {
        public void run() {}
        public void runWithArgs(String first, String second) {}
        public static void runStatic() {}
    }

    /* Wrapper of above class */
    public static class ClassWithVoidMethodsWrapper extends WrapperBase {
        ClassWithVoidMethodsWrapper(Object wrappedInstance) { super(wrappedInstance); }

        public Class<?> getWrappedClass() { return ClassWithVoidMethods.class; }
    }

    /* used for reflection when we need exceptions thrown */
    public static class ExceptionThrowingType {
        private static final Class<?> EXCEPTION_TYPE_THROWN = UnsupportedOperationException.class;

        public ExceptionThrowingType(Boolean shouldThrowException) {
            if (shouldThrowException) {
                throwAnExceptionForMe();
            }
        }

        public void throwExceptionFromVoidMethod() { ExceptionThrowingType.staticallyThrowAnException(); }

        public String throwAnExceptionForMe() {
            return ExceptionThrowingType.staticallyThrowAnException();
        }

        public static String staticallyThrowAnException() {
            throw new UnsupportedOperationException();
        }

        public static Class<?> getExceptionTypeThrown() {
            return EXCEPTION_TYPE_THROWN;
        }
    }

    /* wrap the above in the WrapperBase pattern */
    public static class ExceptionThrowingTypeWrapper extends WrapperBase {
        ExceptionThrowingTypeWrapper(Boolean shouldThrowException) {
            super(new ExceptionThrowingType(shouldThrowException));
        }

        public Class<?> getWrappedClass() {
            return ExceptionThrowingType.class;
        }
    }

    // tests

    // constructor

    @Test
    void constructor_withInstanceOfIncorrectType_throwsException() {
        // GIVEN - instance of incorrect class
        Integer wrongType = 1;

        // WHEN + THEN - IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> new StringWrapper(wrongType));
    }

    // equals()

    @Test
    void equals_withEqualInstance_returnsTrue() {
        // GIVEN - two equal instances of same subclass
        IntegerWrapper firstInt = new IntegerWrapper(1);
        IntegerWrapper secondInt = new IntegerWrapper(1);

        // WHEN
        boolean result = firstInt.equals(secondInt);

        // THEN
        assertTrue(result);
    }

    @Test
    void equals_withUnequalInstance_returnsFalse() {
        // GIVEN - two disequal instances of same subclass
        IntegerWrapper firstInt = new IntegerWrapper(1);
        IntegerWrapper secondInt = new IntegerWrapper(2);

        // WHEN
        boolean result = firstInt.equals(secondInt);

        // THEN
        assertFalse(result);
    }

    @Test
    void equals_withDifferentSubclass_returnsFalse() {
        // GIVEN - two different wrapper class instances
        StringWrapper stringWrapper = new StringWrapper("1");
        IntegerWrapper integerWrapper = new IntegerWrapper(1);

        // WHEN
        boolean result = stringWrapper.equals(integerWrapper);

        // THEN
        assertFalse(result);
    }

    @Test
    void equals_withNull_returnsFalse() {
        // GIVEN - wrapper instance
        StringWrapper stringWrapper = new StringWrapper("hello");

        // WHEN
        boolean result = stringWrapper.equals(null);

        // THEN
        assertFalse(result);
    }

    @Test
    void equals_onNull_throwsException() {
        // GIVEN - wrapper of null
        StringWrapper nullWrapper = new StringWrapper(null);

        // WHEN + THEN - NPE
        assertThrows(NullPointerException.class, () -> nullWrapper.equals(null));
    }

    //  hashCode()

    @Test
    void hashCode_returnsWrappedObjectHashCode() {
        // GIVEN
        // value to wrap
        String value = "hashMe";
        // hashCode of value to wrap
        int valueHashCode = value.hashCode();
        // wrapper
        StringWrapper stringWrapper = new StringWrapper(value);

        // WHEN
        int result = stringWrapper.hashCode();

        // THEN
        assertEquals(valueHashCode, result, "Expected wrapper's hashCode to match wrapped instance's");
    }

    //  toString()

    @Test
    void toString_returnsSomethingIncludingWrappedInstanceToString() {
        // GIVEN
        // value to wrap
        Integer value = 105;
        // value's toString
        String valueToString = value.toString();
        // wrapped value
        IntegerWrapper integerWrapper = new IntegerWrapper(value);

        // WHEN
        String result = integerWrapper.toString();

        // THEN
        assertTrue(
                result.contains(valueToString),
                String.format(
                        "Expected wrapper's toString ('%s') to include wrapped instance's ('%s')",
                        result,
                        valueToString)
        );
    }

    // isNull()

    @Test
    void isNull_onNullWrapper_returnsTrue() {
        // GIVEN - null wrapper
        StringWrapper nullWrapper = new StringWrapper(null);

        // WHEN
        boolean result = nullWrapper.isNull();

        // THEN
        assertTrue(result);
    }

    @Test
    void isNull_onNonNullWrapper_returnsFalse() {
        // GIVEN - null wrapper
        StringWrapper nullWrapper = new StringWrapper("not null");

        // WHEN
        boolean result = nullWrapper.isNull();

        // THEN
        assertFalse(result);
    }

    // getWrappedInstance()

    @Test
    void getWrappedInstance_returnsSameInstanceProvidedConstructor() {
        // GIVEN
        // wrapped instance
        Integer value = 314159;
        // wrapper
        IntegerWrapper integerWrapper = new IntegerWrapper(value);

        // WHEN
        Object result = integerWrapper.getWrappedInstance();

        // THEN
        assertSame(value, result);
    }

    // static Constructor<?> getConstructor(final Class<?> wrappedClass, Class<?>... parameterTypes)

    @Test
    void getConstructor_forExistingNoArgConstructor_returnsConstructor() {
        // GIVEN
        // WHEN - try to get no-args constructor
        Constructor<?> result = WrapperBase.getConstructor(String.class);

        // THEN
        assertNotNull(result);
        assertDoesNotThrow(() -> result.newInstance());
    }

    @Test
    void getConstructor_forExistingArgConstructor_returnsConstructor() {
        // GIVEN
        // class to request constructor from
        Class<?> clazz = String.class;
        // type to specify as constructor argument type
        Class<?> argType = String.class;

        // WHEN - try to get String-arg constructor
        Constructor<?> result = WrapperBase.getConstructor(clazz, argType);

        // THEN
        assertNotNull(result);
        assertDoesNotThrow(() -> result.newInstance("allez"));
    }

    @Test
    void getConstructor_forNonexistentConstructor_assertFires() {
        // GIVEN
        // class to request constructor from
        Class<?> clazz = String.class;
        // type to specify as constructor argument type
        Class<?> nonexistentArgType = WrapperBase.class;

        // WHEN - try to get a constructor that doesn't exist
        // THEN - assertion failure
        assertThrows(AssertionFailedError.class, () -> WrapperBase.getConstructor(clazz, nonexistentArgType));
    }

    // Method findMethod(final String methodName, Class<?>... parameterTypes)

    @Test
    void getMethod_forExistingNoArgMethod_returnsMethod() {
        // GIVEN
        // method name
        String methodName = "length";
        // wrapper
        StringWrapper stringWrapper = new StringWrapper("elio");

        // WHEN
        Method result = stringWrapper.getMethod(methodName);

        // THEN
        assertEquals(methodName, result.getName());
    }

    @Test
    void getMethod_forExistingArgMethod_returnsMethod() {
        // GIVEN
        // method name
        String methodName = "contains";
        // wrapper
        StringWrapper stringWrapper = new StringWrapper("elio");

        // WHEN
        Method result = stringWrapper.getMethod(methodName);

        // THEN
        assertEquals(methodName, result.getName());
    }

    @Test
    void getMethod_forNonExistentMethod_assertFires() {
        // GIVEN
        // method name
        String methodName = "runToTheCoffeeShopAndGetSomeHotCocoa";
        // wrapper
        StringWrapper stringWrapper = new StringWrapper("elio");

        // WHEN - attempt to get nonexistent method
        // THEN
        // exception thrown
        try {
            stringWrapper.getMethod(methodName);
        } catch (AssertionFailedError e) {
            // message contains class name
            assertTrue(
                    e.getMessage().contains(stringWrapper.getWrappedClass().getSimpleName()),
                    String.format(
                            "Expected exception message to include class type ('%s') but was '%s'",
                            stringWrapper.getWrappedClass().getSimpleName(),
                            e.getMessage())
            );
            // message contains method name
            assertTrue(
                    e.getMessage().contains(methodName),
                    String.format(
                            "Expected exception message to include missing method name ('%s') but was '%s'",
                            methodName,
                            e.getMessage())
            );

            // all good!
            return;
        }

        fail("Should have thrown assertion failed error");
    }

    // Object invokeInstanceMethodWithReturnValue(final Method method, Object... args)

    @Test
    void invokeInstanceMethodWithReturnValue_onNoArgMethod_returnsExpectedValue() {
        // GIVEN
        // wrapped value
        String value = "ahoy";
        // wrapper
        StringWrapper stringWrapper = new StringWrapper(value);
        // method to invoke
        Method method = stringWrapper.getMethod("length");

        // WHEN
        Object result = stringWrapper.invokeInstanceMethodWithReturnValue(method);

        // THEN
        assertEquals(value.length(), result);
    }

    @Test
    void invokeInstanceMethodWithReturnValue_onArgMethod_returnsExpectedValue() {
        // GIVEN
        // wrapped value
        String value = "ahoy";
        // substring to search for
        String searchString = "hoy";
        // wrapper
        StringWrapper stringWrapper = new StringWrapper(value);
        // method to invoke
        Method method = stringWrapper.getMethod("contains");

        // WHEN
        Object result = stringWrapper.invokeInstanceMethodWithReturnValue(method, searchString);

        // THEN
        assertEquals(value.contains(searchString), result);
    }

    @Test
    void invokeInstanceMethodWithReturnValue_onNullWrapper_throwsException() {
        // GIVEN
        // null wrapper
        StringWrapper nullWrapper = new StringWrapper(null);
        // method to attempt to invoke
        Method method = nullWrapper.getMethod("length");

        // WHEN - invoke method
        // THEN - exception thrown
        assertThrows(NullPointerException.class, () -> nullWrapper.invokeInstanceMethodWithReturnValue(method));
    }

    @Test
    void invokeInstanceMethodWithReturnValue_onMethodThrowingException_assertFires() {
        // GIVEN
        // method name
        String methodName = "throwAnExceptionForMe";
        // exception-throwing wrapper
        ExceptionThrowingTypeWrapper exceptionThrowingTypeWrapper = new ExceptionThrowingTypeWrapper(false);
        // method to call (that throws exception)
        Method method = exceptionThrowingTypeWrapper.getMethod(methodName);

        // WHEN - invoke method
        // THEN - assertion failure
        try {
            exceptionThrowingTypeWrapper.invokeInstanceMethodWithReturnValue(method);
        } catch (AssertionFailedError e) {
            // message contains class name
            assertTrue(
                    e.getMessage().contains(exceptionThrowingTypeWrapper.getWrappedClass().getSimpleName()),
                    String.format(
                            "Expected exception message to include class name ('%s') but was '%s'",
                            exceptionThrowingTypeWrapper.getWrappedClass().getSimpleName(),
                            e.getMessage())
            );

            // message contains method name
            assertTrue(
                    e.getMessage().contains(methodName),
                    String.format(
                            "Expected exception message to include method name ('%s') but was '%s'",
                            methodName,
                            e.getMessage())
            );

            // message contains exception name
            assertTrue(
                    e.getMessage().contains(ExceptionThrowingType.getExceptionTypeThrown().getSimpleName()),
                    String.format(
                            "Expected exception message to include exception name ('%s') but was '%s'",
                            ExceptionThrowingType.getExceptionTypeThrown().getSimpleName(),
                            e.getMessage())
            );

            // all good!
            return;
        }

        fail("Should have thrown assertion failed error");
    }

    // void invokeVoidInstanceMethod(final Method method, Object...arg)

    @Test
    void invokeVoidInstanceMethod_onNoArgMethod_callsWithNoException() {
        // GIVEN
        // wrapped value
        ClassWithVoidMethods value = mock(ClassWithVoidMethods.class);
        // wrapper
        ClassWithVoidMethodsWrapper wrapper = new ClassWithVoidMethodsWrapper(value);
        // method to invoke
        Method method = wrapper.getMethod("run");

        // WHEN
        wrapper.invokeVoidInstanceMethod(method);

        // THEN
        verify(value).run();
    }

    @Test
    void invokeVoidInstanceMethod_onArgMethod_returnsExpectedValue() {
        // GIVEN
        // wrapped value
        ClassWithVoidMethods value = mock(ClassWithVoidMethods.class);
        // wrapper
        ClassWithVoidMethodsWrapper wrapper = new ClassWithVoidMethodsWrapper(value);
        // method to invoke
        Method method = wrapper.getMethod("runWithArgs");
        // arguments to method
        String arg1 = "one";
        String arg2 = "two";

        // WHEN
        wrapper.invokeVoidInstanceMethod(method, arg1, arg2);

        // THEN
        verify(value).runWithArgs(arg1, arg2);
    }

    @Test
    void invokeVoidInstanceMethod_onNullWrapper_throwsException() {
        // GIVEN
        // null wrapper
        ClassWithVoidMethodsWrapper nullWrapper = new ClassWithVoidMethodsWrapper(null);
        // method to attempt to invoke
        Method method = nullWrapper.getMethod("run");

        // WHEN - invoke method
        // THEN - exception thrown
        assertThrows(NullPointerException.class, () -> nullWrapper.invokeVoidInstanceMethod(method));
    }

    @Test
    void invokeVoidInstanceMethod_onMethodThrowingException_assertFires() {
        // GIVEN
        // method name
        String methodName = "throwExceptionFromVoidMethod";
        // exception-throwing wrapper
        ExceptionThrowingTypeWrapper exceptionThrowingTypeWrapper = new ExceptionThrowingTypeWrapper(false);
        // method to call (that throws exception)
        Method method = exceptionThrowingTypeWrapper.getMethod(methodName);

        // WHEN - invoke method
        // THEN - assertion failure
        try {
            exceptionThrowingTypeWrapper.invokeVoidInstanceMethod(method);
        } catch (AssertionFailedError e) {
            // message contains class name
            assertTrue(
                e.getMessage().contains(exceptionThrowingTypeWrapper.getWrappedClass().getSimpleName()),
                String.format(
                    "Expected exception message to include class name ('%s') but was '%s'",
                    exceptionThrowingTypeWrapper.getWrappedClass().getSimpleName(),
                    e.getMessage())
            );

            // message contains method name
            assertTrue(
                e.getMessage().contains(methodName),
                String.format(
                    "Expected exception message to include method name ('%s') but was '%s'",
                    methodName,
                    e.getMessage())
            );

            // message contains exception name
            assertTrue(
                e.getMessage().contains(ExceptionThrowingType.getExceptionTypeThrown().getSimpleName()),
                String.format(
                    "Expected exception message to include exception name ('%s') but was '%s'",
                    ExceptionThrowingType.getExceptionTypeThrown().getSimpleName(),
                    e.getMessage())
            );

            // all good!
            return;
        }

        fail("Should have thrown assertion failed error");
    }

    // Object invokeStaticMethodWithReturnValue(final Method method, Object ...args)

    @Test
    public void invokeStaticMethodWithReturnValue_wrapperMethodThrowsException_throwsAssertionError() throws Exception {
        // GIVEN
        Method method = ExceptionThrowingType.class.getMethod("staticallyThrowAnException");

        // WHEN + THEN
        assertThrows(AssertionFailedError.class, () -> WrapperBase.invokeStaticMethodWithReturnValue(method));
    }

    @Test
    public void invokeStaticMethodWithReturnValue_callMethodStatically_returnsValue() throws Exception {
        // GIVEN
        Method method = Integer.class.getMethod("compare", int.class, int.class);

        // WHEN
        Object result = WrapperBase.invokeStaticMethodWithReturnValue(method, 4, 5);

        // THEN
        assertEquals(-1, result);
    }
}
