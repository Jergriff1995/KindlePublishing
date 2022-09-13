package com.amazon.ata.test.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MethodQueryTest {
    private static final String FIRST_METHOD_NAME = "firstMethod";
    private static final String SECOND_METHOD_NAME = "secondMethod";

    private static final String VOID_RETURN_METHOD_NAME = "getVoid";
    private static final String ANOTHER_VOID_RETURN_METHOD_NAME = "anotherGetVoid";
    private static final String STRING_RETURN_METHOD_NAME = "getString";
    private static final String ANOTHER_STRING_RETURN_METHOD_NAME = "getAnotherString";
    private static final String INTEGER_RETURN_METHOD_NAME = "getInteger";

    class OneMethodClass {
        public void firstMethod() {}
    }

    class TwoMethodClass {
        public void firstMethod() {}
        public String secondMethod() { return "secondMethod!"; }
    }

    class VoidAndTypedReturnMethodsClass {
        public void getVoid() {}
        public void anotherGetVoid() {}
        public String getString() { return "a string"; }
        public String getAnotherString() { return "another string"; }
        public Integer getInteger() { return 1; }
    }

    @Test
    void inType_withType_filtersOnlyByType() {
        // GIVEN
        Class<?> clazz = TwoMethodClass.class;
        Set<String> expectedMethodNames = ImmutableSet.of(FIRST_METHOD_NAME, SECOND_METHOD_NAME);

        // WHEN
        MethodQuery methodQuery = MethodQuery.inType(clazz);

        // THEN
        Set<Method> methods = methodQuery.findMethods();
        assertMethodsContainOnlyExpectedMethods(methods, expectedMethodNames);
    }

    @Test
    void inType_withNullType_throwsException() {
        // GIVEN
        // WHEN + THEN - null type results in exception
        assertThrows(IllegalArgumentException.class, () -> MethodQuery.inType(null));
    }

    @Test
    void withReturnType_withType_filtersByReturnType() {
        // GIVEN
        Class<?> clazz = VoidAndTypedReturnMethodsClass.class;
        Set<String> expectedMethodNames = ImmutableSet.of(STRING_RETURN_METHOD_NAME, ANOTHER_STRING_RETURN_METHOD_NAME);

        // WHEN
        MethodQuery methodQuery = MethodQuery.inType(clazz)
                                             .withReturnType(String.class);

        // THEN
        Set<Method> methods = methodQuery.findMethods();
        assertMethodsContainOnlyExpectedMethods(methods, expectedMethodNames);
    }

    @Test
    void withReturnType_withNull_throwsException() {
        // GIVEN
        Class<?> clazz = OneMethodClass.class;

        // WHEN + THEN - null return type results in exception
        assertThrows(IllegalArgumentException.class, () -> MethodQuery.inType(clazz).withReturnType(null));
    }

    @Test
    void withReturnType_calledMultipleTimes_throwsException() {
        // GIVEN - method query with return type already specified
        MethodQuery methodQuery = MethodQuery.inType(TwoMethodClass.class).withReturnType(String.class);

        // WHEN + THEN - calling again throws exception
        assertThrows(IllegalStateException.class, () -> methodQuery.withReturnType(Integer.class));
    }

    @Test
    void withReturnType_calledAfterWithVoidReturnType_throwsException() {
        // GIVEN - method query with VOID return type already specified
        MethodQuery methodQuery = MethodQuery.inType(TwoMethodClass.class).withVoidReturnType();

        // WHEN + THEN - setting return type throws exception
        assertThrows(IllegalStateException.class, () -> methodQuery.withReturnType(Integer.class));
    }

    @Test
    void withVoidReturnType_filtersByVoidReturnType() {
        // GIVEN
        Class<?> clazz = VoidAndTypedReturnMethodsClass.class;
        Set<String> expectedMethodNames = ImmutableSet.of(VOID_RETURN_METHOD_NAME, ANOTHER_VOID_RETURN_METHOD_NAME);

        // WHEN
        MethodQuery methodQuery = MethodQuery.inType(clazz)
                                             .withVoidReturnType();

        // THEN
        Set<Method> methods = methodQuery.findMethods();
        assertMethodsContainOnlyExpectedMethods(methods, expectedMethodNames);
    }

    @Test
    void withVoidReturnType_calledMultipleTimes_throwsException() {
        // GIVEN - method query with return type already specified
        MethodQuery methodQuery = MethodQuery.inType(TwoMethodClass.class).withVoidReturnType();

        // WHEN + THEN - calling again throws exception
        assertThrows(IllegalStateException.class, methodQuery::withVoidReturnType);
    }

    @Test
    void withVoidReturnType_calledAfterWithReturnType_throwsException() {
        // GIVEN - method query with NON-VOID return type already specified
        MethodQuery methodQuery = MethodQuery.inType(TwoMethodClass.class).withReturnType(Integer.class);

        // WHEN + THEN - setting return type throws exception
        assertThrows(IllegalStateException.class, methodQuery::withVoidReturnType);
    }

    @Test
    void withExactName_withMatchingName_matchesMethod() {
        // GIVEN
        Class<?> clazz = VoidAndTypedReturnMethodsClass.class;

        // WHEN
        MethodQuery methodQuery = MethodQuery.inType(clazz)
                                             .withExactName(INTEGER_RETURN_METHOD_NAME);

        // THEN
        Method method = methodQuery.findMethod();
        assertEquals(INTEGER_RETURN_METHOD_NAME, method.getName());
    }

    @Test
    void withExactName_withCloseButNonMatchingName_doesNotMatchMethod() {
        // GIVEN
        Class<?> clazz = OneMethodClass.class;
        String incorrectMethodName = FIRST_METHOD_NAME.substring(0, FIRST_METHOD_NAME.length() - 2);

        // WHEN
        MethodQuery methodQuery = MethodQuery.inType(clazz)
                                             .withExactName(incorrectMethodName);

        // THEN
        Set<Method> methods = methodQuery.findMethods();
        assertEquals(
            0,
            methods.size(),
            String.format(
                "Expected withExactName(%s) not to match any methods, but matches were returned: {%s}",
                incorrectMethodName,
                methods.toString())
        );
    }

    @Test
    void withExactName_withNull_throwsException() {
        // GIVEN
        Class<?> clazz = OneMethodClass.class;

        // WHEN + THEN - null return type results in exception
        assertThrows(IllegalArgumentException.class, () -> MethodQuery.inType(clazz).withExactName(null));
    }

    @Test
    void withExactName_withEmptyString_throwsException() {
        // GIVEN
        Class<?> clazz = OneMethodClass.class;

        // WHEN + THEN - null return type results in exception
        assertThrows(IllegalArgumentException.class, () -> MethodQuery.inType(clazz).withExactName(""));
    }

    @Test
    void withExactName_calledMultipleTimes_throwsException() {
        // GIVEN - method query with name already specified
        MethodQuery methodQuery = MethodQuery.inType(TwoMethodClass.class).withExactName(FIRST_METHOD_NAME);

        // WHEN + THEN - calling again throws exception
        assertThrows(IllegalStateException.class, () -> methodQuery.withExactName(FIRST_METHOD_NAME));
    }

    @Test
    void withExactName_calledAfterWithNameContaining_throwsException() {
        // GIVEN - method query with name already specified
        MethodQuery methodQuery = MethodQuery.inType(TwoMethodClass.class).withNameContaining("first");

        // WHEN + THEN - calling throws exception
        assertThrows(IllegalStateException.class, () -> methodQuery.withExactName(FIRST_METHOD_NAME));
    }

    @Test
    void withNameContaining_withExactName_matchesMethod() {
        // GIVEN
        Class<?> clazz = TwoMethodClass.class;

        // WHEN
        MethodQuery methodQuery = MethodQuery.inType(clazz)
            .withNameContaining(SECOND_METHOD_NAME);

        // THEN
        Method method = methodQuery.findMethod();
        assertEquals(
            SECOND_METHOD_NAME,
            method.getName(),
            String.format(
                "Expected findMethod to return method %s when searching for '%s', but returned %s",
                SECOND_METHOD_NAME,
                SECOND_METHOD_NAME,
                method.getName())
        );
    }

    @Test
    void withNameContaining_withBeginningSubstring_matchesMethod() {
        // GIVEN
        Class<?> clazz = TwoMethodClass.class;
        String methodNameSubstring = SECOND_METHOD_NAME.substring(0, SECOND_METHOD_NAME.length() - 3);

        // WHEN
        MethodQuery methodQuery = MethodQuery.inType(clazz)
            .withNameContaining(methodNameSubstring);

        // THEN
        Method method = methodQuery.findMethod();
        assertEquals(
            SECOND_METHOD_NAME,
            method.getName(),
            String.format(
                "Expected findMethod to return method %s when searching for '%s', but returned %s",
                SECOND_METHOD_NAME,
                methodNameSubstring,
                method.getName())
        );
    }

    @Test
    void withNameContaining_withMiddleSubstring_matchesMethod() {
        // GIVEN
        Class<?> clazz = TwoMethodClass.class;
        String methodNameSubstring = SECOND_METHOD_NAME.substring(2, SECOND_METHOD_NAME.length() - 2);

        // WHEN
        MethodQuery methodQuery = MethodQuery.inType(clazz)
            .withNameContaining(methodNameSubstring);

        // THEN
        Method method = methodQuery.findMethod();
        assertEquals(
            SECOND_METHOD_NAME,
            method.getName(),
            String.format(
                "Expected findMethod to return method %s when searching for '%s', but returned %s",
                SECOND_METHOD_NAME,
                methodNameSubstring,
                method.getName())
        );
    }

    @Test
    void withNameContaining_withSubstringMatchingMultipleMethods_matchesMethods() {
        // GIVEN
        Class<?> clazz = VoidAndTypedReturnMethodsClass.class;
        String methodNameSubstring = "get";
        Set<String> expectedMethodNames = ImmutableSet.of(
                                              VOID_RETURN_METHOD_NAME,
                                              STRING_RETURN_METHOD_NAME,
                                              ANOTHER_STRING_RETURN_METHOD_NAME,
                                              INTEGER_RETURN_METHOD_NAME
        );

        // WHEN
        MethodQuery methodQuery = MethodQuery.inType(clazz)
            .withNameContaining(methodNameSubstring);

        // THEN
        Set<Method> methods = methodQuery.findMethods();
        assertMethodsContainOnlyExpectedMethods(methods, expectedMethodNames);
    }

    @Test
    void withNameContaining_calledMultipleTimes_filtersByAllSubstrings() {
        // GIVEN
        Class<?> clazz = VoidAndTypedReturnMethodsClass.class;
        Set<String> expectedMethodNames = ImmutableSet.of(
            STRING_RETURN_METHOD_NAME,
            ANOTHER_STRING_RETURN_METHOD_NAME
        );

        // WHEN
        MethodQuery methodQuery = MethodQuery.inType(clazz)
            .withNameContaining("get")
            .withNameContaining("String");

        // THEN
        Set<Method> methods = methodQuery.findMethods();
        assertMethodsContainOnlyExpectedMethods(methods, expectedMethodNames);
    }

    @Test
    void withNameContaining_withNull_throwsException() {
        // GIVEN
        Class<?> clazz = OneMethodClass.class;

        // WHEN + THEN - null return type results in exception
        assertThrows(IllegalArgumentException.class, () -> MethodQuery.inType(clazz).withNameContaining(null));
    }

    @Test
    void withNameContaining_withEmptyString_throwsException() {
        // GIVEN
        Class<?> clazz = OneMethodClass.class;

        // WHEN + THEN - null return type results in exception
        assertThrows(IllegalArgumentException.class, () -> MethodQuery.inType(clazz).withNameContaining(""));
    }

    @Test
    void withNameContaining_calledAfterWithExactName_throwsException() {
        // GIVEN - method query with name already specified
        MethodQuery methodQuery = MethodQuery.inType(TwoMethodClass.class).withExactName(FIRST_METHOD_NAME);

        // WHEN + THEN - calling throws exception
        assertThrows(IllegalStateException.class, () -> methodQuery.withNameContaining("first"));
    }

    @Nested
    class WithExactArgTypes {
        private static final String NO_ARGS_METHOD_NAME = "withNoArgs";
        private static final String STRING_THEN_INTEGER_ARGS_METHOD_NAME = "withTwoArgs";
        private static final String INTEGER_THEN_STRING_ARGS_METHOD_NAME = "withTwoArgsAndReturn";

        class ArgedMethodsClass {
            public void withNoArgs() {}
            public void withString(String s) {}
            public void withTwoArgs(String s, Integer i) {}
            public String withTwoArgsAndReturn(Integer i, String s) { return s + i; }
        }

        @Test
        void withExactArgTypes_withNoArgs_matchesNoArgsMethod() {
            // GIVEN
            Class<?> clazz = ArgedMethodsClass.class;

            // WHEN
            MethodQuery methodQuery = MethodQuery.inType(clazz)
                .withExactArgTypes(Collections.emptyList());

            // THEN
            Method method = methodQuery.findMethod();
            assertEquals(
                NO_ARGS_METHOD_NAME,
                method.getName(),
                String.format("Expected to return method %s but found instead: %s", NO_ARGS_METHOD_NAME, method.toString())
            );
        }

        @Test
        void withExactArgTypes_withArgsInDifferentOrder_matchesMethod() {
            // GIVEN
            Class<?> clazz = ArgedMethodsClass.class;
            Set<String> expectedMethodNames = ImmutableSet.of(
                STRING_THEN_INTEGER_ARGS_METHOD_NAME, INTEGER_THEN_STRING_ARGS_METHOD_NAME
            );

            // WHEN
            MethodQuery methodQuery = MethodQuery.inType(clazz)
                .withExactArgTypes(ImmutableList.of(String.class, Integer.class));

            // THEN
            Set<Method> methods = methodQuery.findMethods();
            assertMethodsContainOnlyExpectedMethods(methods, expectedMethodNames);
        }

        @Test
        void withExactArgTypes_withSubsetOfArgs_doesNotMatchMethod() {
            // GIVEN
            Class<?> clazz = ArgedMethodsClass.class;

            // WHEN
            MethodQuery methodQuery = MethodQuery.inType(clazz)
                .withExactArgTypes(ImmutableList.of(Integer.class));

            // THEN
            Set<Method> methods = methodQuery.findMethods();
            assertTrue(
                methods.isEmpty(),
                String.format(
                    "Expected no methods to return for withExactArgTypes(Integer) but found: %s", methods.toString())
            );
        }

        @Test
        void withExactArgTypes_withNullListOfArgTypes_throwsException() {
            // GIVEN
            Class<?> clazz = ArgedMethodsClass.class;

            // WHEN + THEN - null return type results in exception
            assertThrows(IllegalArgumentException.class, () -> MethodQuery.inType(clazz).withExactArgTypes(null));
        }

        @Test
        void withExactArgTypes_withNullArgType_throwsException() {
            // GIVEN
            Class<?> clazz = ArgedMethodsClass.class;
            List<Class<?>> listIncludingNull = new ArrayList<>();
            listIncludingNull.add(String.class);
            listIncludingNull.add(Integer.class);
            listIncludingNull.add(null);
            listIncludingNull.add(List.class);

            // WHEN + THEN - null return type results in exception
            assertThrows(
                IllegalArgumentException.class,
                () -> MethodQuery.inType(clazz).withExactArgTypes(listIncludingNull)
            );
        }

        @Test
        void withExactArgTypes_calledMultipleTimes_throwsException() {
            // GIVEN - method query with exact args already specified
            MethodQuery methodQuery =
                MethodQuery.inType(ArgedMethodsClass.class).withExactArgTypes(Collections.emptyList());

            // WHEN + THEN - calling again throws exception
            assertThrows(IllegalStateException.class, () -> methodQuery.withExactArgTypes(ImmutableList.of(int.class)));
        }

        @Test
        void withExactArgTypes_withTooManyArgs_throwsException() {
            // GIVEN
            List<Class<?>> tooManyArgs = ImmutableList.of(
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                String.class
            );

            // WHEN + THEN - too many arg types throws exception
            assertThrows(
                IllegalArgumentException.class,
                () -> MethodQuery.inType(String.class).withExactArgTypes(tooManyArgs)
            );
        }
    }

    @Test
    void findMethodOrFail_whenZeroMethodsMatch_assertFires() {
        // GIVEN
        // WHEN + THEN - no match assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> MethodQuery.inType(String.class).withExactName("whoPutThisHere").findMethodOrFail()
        );
    }

    @Test
    void findMethodOrFail_whenOneMethodMatches_returnsMethod() {
        // GIVEN
        // WHEN
        Method method = MethodQuery.inType(OneMethodClass.class)
            .withExactName(FIRST_METHOD_NAME)
            .findMethodOrFail();

        // THEN
        assertEquals(
            FIRST_METHOD_NAME,
            method.getName(),
            String.format("Expected method %s but got: %s", FIRST_METHOD_NAME, method.toString())
        );
    }

    @Test
    void findMethodOrFail_whenMultipleMethodsMatch_assertFires() {
        // GIVEN
        // WHEN + THEN - too many matches assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> MethodQuery.inType(String.class).withNameContaining("valueOf").findMethodOrFail()
        );
    }

    @Test
    void findMethod_whenZeroMethodsMatch_throwsException() {
        // GIVEN
        // WHEN + THEN - no match throws exception
        assertThrows(
            NoMethodFoundException.class,
            () -> MethodQuery.inType(String.class).withExactName("whoPutThisHere").findMethod()
        );
    }

    @Test
    void findMethod_whenOneMethodMatches_returnsMethod() {
        // GIVEN
        // WHEN
        Method method = MethodQuery.inType(OneMethodClass.class)
            .withExactName(FIRST_METHOD_NAME)
            .findMethod();

        // THEN
        assertEquals(
            FIRST_METHOD_NAME,
            method.getName(),
            String.format("Expected method %s but got: %s", FIRST_METHOD_NAME, method.toString())
        );
    }

    @Test
    void findMethod_whenMultipleMethodsMatch_throwsException() {
        // GIVEN
        // WHEN + THEN - too many matches throws exception
        assertThrows(
            MultipleMethodsFoundException.class,
            () -> MethodQuery.inType(String.class).withNameContaining("valueOf").findMethod()
        );
    }

    @Nested
    class FindMethods {
        private static final String OVERLOADED_METHOD_NAME = "overloadMe";
        private static final String OTHER_OVERLOADED_METHOD_NAME = "overloadMeToo";

        class OverloadedMethodsClass {
            public void overloadMe() {}
            public void overloadMe(String s) {}
            public String overloadMe(Integer i) { return i.toString(); }
            public String overloadMeToo(String s) { return s; }
            public void overloadMeToo(Integer i) {}
        }

        @Test
        void findMethods_whenZeroMethodsMatch_returnsEmptySet() {
            // GIVEN
            // WHEN
            Set<Method> methods = MethodQuery.inType(String.class)
                .withExactName("whoPutThisHere")
                .findMethods();

            // THEN
            assertTrue(methods.isEmpty(), String.format("Expected no methods returned but found: %s", methods.toString()));
        }

        @Test
        void findMethods_whenMultipleMethodsMatch_returnsMultipleMethods() {
            // GIVEN
            // WHEN
            Set<Method> methods = MethodQuery.inType(String.class)
                .withExactName("valueOf")
                .findMethods();

            // THEN
            assertTrue(
                methods.size() > 1,
                String.format("Expected to find many valueOf methods, but only found: %s", methods.toString())
            );
        }

        @Test
        void findMethods_byNameAndReturnType_appliesAllFilters() {
            // GIVEN
            // WHEN
            Set<Method> methods = MethodQuery.inType(OverloadedMethodsClass.class)
                .withExactName(OVERLOADED_METHOD_NAME)
                .withVoidReturnType()
                .findMethods();

            // THEN
            assertEquals(
                2,
                methods.size(),
                String.format("Expected two overloaded methods to match but got %s", methods.toString())
            );
        }

        @Test
        void findMethods_byNameContainingAndArgTypes_appliesAllFilters() {
            // GIVEN
            Set<String> expectedMethodNames = ImmutableSet.of(OVERLOADED_METHOD_NAME, OTHER_OVERLOADED_METHOD_NAME);
            // WHEN
            Set<Method> methods = MethodQuery.inType(OverloadedMethodsClass.class)
                .withNameContaining("overload")
                .withExactArgTypes(ImmutableList.of(String.class))
                .findMethods();

            // THEN
            assertMethodsContainOnlyExpectedMethods(methods, expectedMethodNames);
        }

        @Test
        void findMethods_byNameContainingReturnTypeAndArgTypes_appliesAllFilters() {
            // GIVEN
            // WHEN
            Set<Method> methods = MethodQuery.inType(OverloadedMethodsClass.class)
                .withNameContaining("overload")
                .withVoidReturnType()
                .withExactArgTypes(ImmutableList.of(String.class))
                .findMethods();

            // THEN
            assertEquals(
                1,
                methods.size(),
                String.format("Expected only one of overloaded methods to match but got %s", methods.toString())
            );
        }
    }
    private void assertMethodsContainOnlyExpectedMethods(Set<Method> methods, Set<String> expectedMethodNames) {
        Set<String> methodNames = methods.stream()
            .map(Method::getName)
             .collect(Collectors.toSet());
        assertEquals(
            expectedMethodNames,
                methodNames,
            String.format("Expected findMethods to return only methods {%s}, but found {%s}",
                expectedMethodNames.toString(),
                methodNames.toString())
        );
    }
}
