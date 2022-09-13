package com.amazon.ata.test.reflect;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MethodInvokerTest {
    /* used for reflection when we need exceptions thrown */
    public static class ExceptionThrowingType {
        private static final Class<?> EXCEPTION_TYPE_THROWN = UnsupportedOperationException.class;

        public ExceptionThrowingType(Boolean shouldThrowException) {
            if (shouldThrowException) {
                throwAnExceptionForMe();
            }
        }

        public String throwAnExceptionForMe() {
            return MethodInvokerTest.ExceptionThrowingType.staticallyThrowAnException();
        }

        public static String staticallyThrowAnException() {
            throw new UnsupportedOperationException();
        }

        public static Class<?> getExceptionTypeThrown() {
            return EXCEPTION_TYPE_THROWN;
        }
    }

    public interface InterfaceType {

    }

    public abstract static class SuperType {

    }

    public static class ImplType implements InterfaceType {

    }

    public static class SubType extends SuperType {

    }
    public static class MethodInvokerTestClass {
        String distinctArgType1;
        Long distinctArgType2;
        InterfaceType implType;
        SuperType subType;
        String arg1;
        String arg2;

        public MethodInvokerTestClass(String distinctArgType1, Long distinctArgType2) {
            this.distinctArgType1 = distinctArgType1;
            this.distinctArgType2 = distinctArgType2;
        }

        public MethodInvokerTestClass(InterfaceType implType, SuperType subType) {
            this.implType = implType;
            this.subType = subType;
        }

        public MethodInvokerTestClass(String argType1, String argType2) {
            this.arg1 = argType1;
            this.arg2 = argType2;
        }
    }

    @Test
    void invokeInstanceMethodWithReturnValue_onNoArgMethod_returnsExpectedValue() {
        // GIVEN
        // value
        String value = "ahoy";
        // method to invoke
        Method method = MethodQuery.inType(String.class).withExactName("length").findMethod();

        // WHEN
        Object result = MethodInvoker.invokeInstanceMethodWithReturnValue(value, method);

        // THEN
        assertEquals(value.length(), result);
    }

    @Test
    void invokeInstanceMethodWithReturnValue_onArgMethod_returnsExpectedValue() {
        // GIVEN
        // value
        String value = "ahoy";
        // substring to search for
        String searchString = "hoy";
        // method to invoke
        Method method = MethodQuery.inType(String.class).withExactName("contains").findMethod();

        // WHEN
        Object result = MethodInvoker.invokeInstanceMethodWithReturnValue(value, method, searchString);

        // THEN
        assertEquals(value.contains(searchString), result);
    }

    @Test
    void invokeInstanceMethodWithReturnValue_onNullValue_throwsException() {
        // GIVEN
        // method to attempt to invoke
        Method method = MethodQuery.inType(String.class).withExactName("length").findMethod();

        // WHEN - invoke method
        // THEN - exception thrown
        assertThrows(
            NullPointerException.class,
            () -> MethodInvoker.invokeInstanceMethodWithReturnValue((Object) null, method)
        );
    }

    @Test
    void invokeInstanceMethodWithReturnValue_onMethodThrowingException_assertFires() {
        // GIVEN
        // method name
        String methodName = "throwAnExceptionForMe";
        // method to call (that throws exception)
        Method method = MethodQuery.inType(MethodInvokerTest.ExceptionThrowingType.class).withExactName(methodName).findMethod();
        // object to call method on
        MethodInvokerTest.ExceptionThrowingType exceptionThrowingType = new MethodInvokerTest.ExceptionThrowingType(false);

        // WHEN - invoke method
        // THEN - assertion failure
        AssertionFailedError e = assertThrows(
            AssertionFailedError.class,
            () -> MethodInvoker.invokeInstanceMethodWithReturnValue(exceptionThrowingType, method),
            "Should have thrown assertion failed error when called method throws exception"
        );
        // message contains class name
        assertTrue(
            e.getMessage().contains(exceptionThrowingType.getClass().getSimpleName()),
            String.format(
                "Expected exception message to include class name ('%s') but was '%s'",
                exceptionThrowingType.getClass().getSimpleName(),
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
            e.getMessage().contains(MethodInvokerTest.ExceptionThrowingType.getExceptionTypeThrown().getSimpleName()),
            String.format(
                "Expected exception message to include exception name ('%s') but was '%s'",
                MethodInvokerTest.ExceptionThrowingType.getExceptionTypeThrown().getSimpleName(),
                e.getMessage())
        );
    }


    @Nested
    static class InvokeStaticMethodWithReturnValue {
        /* a class with simple static methods for static method invocation */
        static class TypeWithStaticMethods {
            public static Integer one() {
                return 1;
            }

            public static Integer onePlus(Integer addend) {
                return one() + addend;
            }
        }

        @Test
        void invokeStaticMethodWithReturnValue_onNoArgMethod_returnsExpectedValue() {
            // GIVEN
            // method to invoke
            Method method = MethodQuery.inType(TypeWithStaticMethods.class).withExactName("one").findMethod();

            // WHEN
            Object result = MethodInvoker.invokeStaticMethodWithReturnValue(method);

            // THEN
            assertEquals(TypeWithStaticMethods.one(), result);
        }

        @Test
        void invokeStaticMethodWithReturnValue_onArgMethod_returnsExpectedValue() {
            // GIVEN
            // value to provide to method
            Integer addend = 41;
            // method to invoke
            Method method = MethodQuery.inType(TypeWithStaticMethods.class).withExactName("onePlus").findMethod();

            // WHEN
            Object result = MethodInvoker.invokeStaticMethodWithReturnValue(method, addend);

            // THEN
            assertEquals(TypeWithStaticMethods.onePlus(addend), result);
        }

        @Test
        void invokeStaticMethodWithReturnValue_onMethodThrowingException_assertFires() {
            // GIVEN
            // method name
            String methodName = "staticallyThrowAnException";
            // method to call (that throws exception)
            Method method = MethodQuery.inType(MethodInvokerTest.ExceptionThrowingType.class).withExactName(methodName).findMethod();

            // WHEN - invoke method
            // THEN - assertion failure
            try {
                MethodInvoker.invokeStaticMethodWithReturnValue(method);
            } catch (AssertionFailedError e) {
                // message contains class name
                assertTrue(
                    e.getMessage().contains(MethodInvokerTest.ExceptionThrowingType.class.getSimpleName()),
                    String.format(
                        "Expected exception message to include class name ('%s') but was '%s'",
                        MethodInvokerTest.ExceptionThrowingType.class.getSimpleName(),
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
                    e.getMessage().contains(MethodInvokerTest.ExceptionThrowingType.getExceptionTypeThrown().getSimpleName()),
                    String.format(
                        "Expected exception message to include exception name ('%s') but was '%s'",
                        MethodInvokerTest.ExceptionThrowingType.getExceptionTypeThrown().getSimpleName(),
                        e.getMessage())
                );

                // all good!
                return;
            }

            fail("Should have thrown assertion failed error");
        }
    }


    // static <T> T invokeConstructor(final Constructor<T> constructor, Object... parameters)

    @Test
    void invokeConstructor_withNoArgConstructor_instantiatesExpectedValue() {
        // GIVEN
        // no-args constructor
        Constructor<?> noArgConstructor = ConstructorQuery.inClass(String.class).withNoArgs().findConstructor();

        // WHEN
        Object result = MethodInvoker.invokeConstructor(noArgConstructor);

        // THEN
        assertEquals(new String(), result);
    }

    @Test
    void invokeConstructor_withArgConstructor_instantiatesExpectedValue() {
        // GIVEN
        // String-arg constructor
        Constructor<?> argConstructor =
            ConstructorQuery.inClass(String.class).withExactArgTypes(ImmutableList.of(String.class)).findConstructor();
        // constructor argument
        String arg = "welcome";

        // WHEN
        Object result = MethodInvoker.invokeConstructor(argConstructor, arg);

        // THEN
        assertEquals(arg, result);
    }

    @Test
    void invokeConstructor_withDistinctArgConstructor_sortsParametersByDeclaredTypes() {
        // GIVEN
        // a distinct, multi-arg constructor
        Constructor<?> argConstructor =
            ConstructorQuery.inClass(MethodInvokerTestClass.class).withExactArgTypes(ImmutableList.of(String.class, Long.class)).findConstructor();
        // constructor argument
        String arg1 = "welcome";
        Long arg2 = 1L;

        // WHEN we pass the parameters out of order
        Object result = MethodInvoker.invokeConstructor(argConstructor, arg2, arg1);

        // THEN we sort the parameters based on the constructor arg types
        assertEquals(MethodInvokerTestClass.class, result.getClass());
        MethodInvokerTestClass testClass = (MethodInvokerTestClass) result;
        assertEquals(arg1, testClass.distinctArgType1);
        assertEquals(arg2, testClass.distinctArgType2);
    }

    @Test
    void invokeConstructor_withInterfaceAndAbstractArgConstructor_sortsParametersByImplAndSubTypes() {
        // GIVEN
        // a distinct, multi-arg constructor
        Constructor<?> argConstructor =
            ConstructorQuery.inClass(MethodInvokerTestClass.class).withExactArgTypes(ImmutableList.of(InterfaceType.class,
                SuperType.class)).findConstructor();
        // constructor argument
        ImplType implType = new ImplType();
        SubType subType = new SubType();

        // WHEN we pass the parameters out of order
        Object result = MethodInvoker.invokeConstructor(argConstructor, subType, implType);

        // THEN we sort the parameters based on the constructor arg types
        assertEquals(MethodInvokerTestClass.class, result.getClass());
        MethodInvokerTestClass testClass = (MethodInvokerTestClass) result;
        assertEquals(implType, testClass.implType);
        assertEquals(subType, testClass.subType);
    }

    @Test
    void invokeConstructor_withDuplicateArgTypeConstructor_doesNotSort() {
        // GIVEN
        // a multi-arg constructor with duplicate types
        Constructor<?> argConstructor =
            ConstructorQuery.inClass(MethodInvokerTestClass.class).withExactArgTypes(ImmutableList.of(String.class,
                String.class)).findConstructor();
        // constructor argument
        String arg1 = "arg1";
        String arg2 = "arg2";

        // WHEN we pass the parameters in a specific order
        Object result = MethodInvoker.invokeConstructor(argConstructor, arg2, arg1);

        // THEN we don't try to sort the parameters
        assertEquals(MethodInvokerTestClass.class, result.getClass());
        MethodInvokerTestClass testClass = (MethodInvokerTestClass) result;
        // we passed arg2 as the first argument
        assertEquals(arg2, testClass.arg1);
        // and arg1 as the second
        assertEquals(arg1, testClass.arg2);
    }

    @Test
    void invokeConstructor_onConstructorThatThrowsException_assertFires() {
        // GIVEN
        // exception-throwing constructor
        Constructor<?> exceptionConstructor = ConstructorQuery.inClass(MethodInvokerTest.ExceptionThrowingType.class)
            .withExactArgTypes(ImmutableList.of(Boolean.class)).findConstructor();
        boolean shouldThrowException = true;

        // WHEN - invoke constructor
        // THEN
        // assertion fails
        try {
            MethodInvoker.invokeConstructor(exceptionConstructor, shouldThrowException);
        } catch (AssertionFailedError e) {
            // message contains class name
            assertTrue(
                e.getMessage().contains(exceptionConstructor.getClass().getSimpleName()),
                String.format(
                    "Expected exception message to include class name ('%s') but was '%s'",
                    exceptionConstructor.getClass().getSimpleName(),
                    e.getMessage())
            );
            // message contains exception class name
            assertTrue(
                e.getMessage().contains(MethodInvokerTest.ExceptionThrowingType.getExceptionTypeThrown().getSimpleName()),
                String.format(
                    "Expected exception message to include exception class name ('%s') but was '%s'",
                    MethodInvokerTest.ExceptionThrowingType.getExceptionTypeThrown().getSimpleName(),
                    e.getMessage())
            );

            // all good!
            return;
        }

        fail("Should have thrown assertion failed error");
    }
}
