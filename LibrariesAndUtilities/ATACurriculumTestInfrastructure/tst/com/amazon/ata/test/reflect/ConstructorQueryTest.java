package com.amazon.ata.test.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ConstructorQueryTest {
    private static class DefaultConstructorClass {}
    private static class NoArgConstructorClass { public NoArgConstructorClass() {} }
    private static class OneArgConstructorClass { public OneArgConstructorClass(String s) {} }
    private static class MultipleConstructorsClass {
        public MultipleConstructorsClass() {}
        public MultipleConstructorsClass(String s) {}
        public MultipleConstructorsClass(Integer i, String s, BigDecimal b) {}
    }
    private static class ReorderedConstructorsClass {
        public ReorderedConstructorsClass(int i, boolean b, String s) {}
        public ReorderedConstructorsClass(boolean b, String s, int i) {}
        public ReorderedConstructorsClass(String s, int i, boolean b) {}
    }

    @Test
    void inClass_withClass_filtersOnlyByClass() {
        // GIVEN
        Set<Constructor<?>> expectedConstructors =
            ImmutableSet.copyOf(MultipleConstructorsClass.class.getConstructors());

        // WHEN
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(MultipleConstructorsClass.class);

        // THEN
        assertQueryFindsExpectedConstructors(constructorQuery, expectedConstructors, "specifying only class");
    }

    @Test
    void inClass_withNullClass_throwsException() {
        // GIVEN
        // WHEN + THEN - null class throws exception
        assertThrows(IllegalArgumentException.class, () -> ConstructorQuery.inClass(null));
    }

    @Test
    void withNoArgs_forClassWithNoConstructor_doesNotMatchConstructors() {
        // GIVEN
        // WHEN
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(DefaultConstructorClass.class)
            .withNoArgs();

        // THEN
        assertQueryFindsNoConstructors(constructorQuery, "finding in class with default constructor");
    }

    @Test
    void withNoArgs_forClassWithNoArgsConstructor_matchesNoArgsConstructor() {
        // GIVEN
        Constructor<?> expectedConstructor =
            NoArgConstructorClass.class.getConstructors()[0];

        // WHEN
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(NoArgConstructorClass.class)
            .withNoArgs();

        // THEN
        assertQueryFindsExpectedConstructor(constructorQuery, expectedConstructor, "finding no-arg constructor");
    }

    @Test
    void withExactArgTypes_withNoArgsOnClassWithNoConstructor_doesNotMatchConstructors() {
        // GIVEN
        // WHEN
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(DefaultConstructorClass.class)
            .withExactArgTypes(Collections.emptyList());

        // THEN
        assertQueryFindsNoConstructors(constructorQuery, "finding in class with default constructor");
    }

    @Test
    void withExactArgTypes_withNoArgs_matchesNoArgsConstructor() {
        // GIVEN
        Constructor<?> expectedConstructor =
            NoArgConstructorClass.class.getConstructors()[0];

        // WHEN
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(NoArgConstructorClass.class)
            .withExactArgTypes(Collections.emptyList());

        // THEN
        assertQueryFindsExpectedConstructor(constructorQuery, expectedConstructor, "finding no-args constructor");
    }

    @Test
    void withExactArgTypes_withArgsInDifferentOrder_matchesConstructor() {
        // GIVEN
        Class[] expectedArgTypes = {Integer.class, String.class, BigDecimal.class};

        // WHEN
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(MultipleConstructorsClass.class)
            .withExactArgTypes(ImmutableList.of(BigDecimal.class, Integer.class, String.class));

        // THEN
        assertQueryFindsConstructorWithArgTypes(
            constructorQuery,
            expectedArgTypes,
            "finding arg types in different order"
        );
        assertEquals(
            1,
            constructorQuery.findConstructors().size(),
            String.format(
                "Expected to find only one constructor, but found %s.%nConstructor query was: %s",
                constructorQuery.findConstructors(),
                constructorQuery)
        );
    }

    @Test
    void withExactArgTypes_forClassWithPermutationsOfSameArgs_matchesAllConstructorsWithSameArgs() {
        // GIVEN
        Set<Constructor<?>> expectedConstructors =
            ImmutableSet.copyOf(ReorderedConstructorsClass.class.getConstructors());

        // WHEN
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(ReorderedConstructorsClass.class)
            .withExactArgTypes(ImmutableList.of(String.class, boolean.class, int.class));

        // THEN
        assertQueryFindsExpectedConstructors(
            constructorQuery,
            expectedConstructors,
            "finding constructors with permutations of same args"
        );
    }

    @Test
    void withExactArgTypes_withSubsetOfArgs_doesNotMatchConstructor() {
        // GIVEN
        List<Class<?>> argTypes = ImmutableList.of(Integer.class, String.class);

        // WHEN
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(MultipleConstructorsClass.class)
            .withExactArgTypes(argTypes);

        // THEN
        Set<Constructor<?>> constructors = constructorQuery.findConstructors();
        assertTrue(
            constructors.isEmpty(),
            String.format(
                "Expected not to find any constructors with params %s but found %s.%n" +
                    "Constructor query was: %s",
                argTypes,
                constructors,
                constructorQuery)
        );
    }

    @Test
    void withExactArgTypes_withNullListOfArgTypes_throwsException() {
        // GIVEN
        // WHEN + THEN - null args list throws exception
        assertThrows(
            IllegalArgumentException.class,
            () -> ConstructorQuery.inClass(String.class).withExactArgTypes(null)
        );
    }

    @Test
    void withExactArgTypes_withNullArgType_throwsException() {
        // GIVEN - arg types list including a null
        List<Class<?>> argTypes = new ArrayList<>();
        argTypes.add(String.class);
        argTypes.add(Boolean.class);
        argTypes.add(null);
        argTypes.add(String.class);

        // WHEN + THEN - including a null throws exception
        assertThrows(
            IllegalArgumentException.class,
            () -> ConstructorQuery.inClass(String.class).withExactArgTypes(argTypes)
        );
    }

    @Test
    void withExactArgTypes_withTooManyArgs_throwsException() {
        // GIVEN
        List<Class<?>> argTypes = ImmutableList.of(
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
            () -> ConstructorQuery.inClass(String.class).withExactArgTypes(argTypes)
        );
    }

    @Test
    void withExactArgTypes_callingTwice_throwsException() {
        // GIVEN - query with arg types already set
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(MultipleConstructorsClass.class)
            .withExactArgTypes(ImmutableList.of(String.class));

        // WHEN + THEN - calling again throws exception
        assertThrows(
            IllegalStateException.class,
            () -> constructorQuery.withExactArgTypes(ImmutableList.of(String.class))
        );
    }

    @Test
    void findConstructorOrFail_whenZeroConstructorsMatch_assertFires() {
        // GIVEN
        // WHEN + THEN - no match assertion failure
        assertThrows(
            AssertionFailedError.class,
            () -> ConstructorQuery.inClass(DefaultConstructorClass.class).findConstructorOrFail()
        );
    }

    @Test
    void findConstructorOrFail_whenOneConstructorMatches_returnsConstructor() {
        // GIVEN
        // - expected constructor
        Constructor<?> expectedConstructor = OneArgConstructorClass.class.getConstructors()[0];
        // - query to return that constructor
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(OneArgConstructorClass.class)
            .withExactArgTypes(ImmutableList.of(String.class));

        // WHEN
        Constructor<?> constructor = constructorQuery.findConstructorOrFail();

        // THEN
        assertExpectedConstructor(
            expectedConstructor,
            constructor,
            "finding single constructor or failing",
            constructorQuery);
    }

    @Test
    void findConstructorOrFail_whenMultipleConstructorsMatch_assertFires() {
        // GIVEN
        // WHEN + THEN - multiple matches assertion failure
        assertThrows(
            AssertionFailedError.class,
            () -> ConstructorQuery.inClass(MultipleConstructorsClass.class).findConstructorOrFail()
        );
    }

    @Test
    void findConstructor_whenZeroConstructorsMatch_assertFires() {
        // GIVEN
        // WHEN + THEN - no match assertion failure
        assertThrows(
            NoConstructorFoundException.class,
            () -> ConstructorQuery.inClass(DefaultConstructorClass.class).findConstructor()
        );
    }

    @Test
    void findConstructor_whenOneConstructorMatches_returnsConstructor() {
        // GIVEN
        // - expected constructor
        Constructor<?> expectedConstructor = OneArgConstructorClass.class.getConstructors()[0];
        // - query to return that constructor
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(OneArgConstructorClass.class)
            .withExactArgTypes(ImmutableList.of(String.class));

        // WHEN
        Constructor<?> constructor = constructorQuery.findConstructor();

        // THEN
        assertExpectedConstructor(
            expectedConstructor,
            constructor,
            "finding single constructor",
            constructorQuery);
    }

    @Test
    void findConstructor_whenMultipleConstructorsMatch_assertFires() {
        // GIVEN
        // WHEN + THEN - multiple matches assertion failure
        assertThrows(
            MultipleConstructorsFoundException.class,
            () -> ConstructorQuery.inClass(MultipleConstructorsClass.class).findConstructor()
        );
    }

    @Test
    void findConstructors_whenZeroConstructorsMatch_returnsEmptySet() {
        // GIVEN
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(DefaultConstructorClass.class);

        // WHEN
        Set<Constructor<?>> constructors = constructorQuery.findConstructors();

        // THEN
        assertTrue(
            constructors.isEmpty(),
            String.format(
                "Expected findConstructors to return empty set but returned %s.%nConstructor query was: %s",
                constructors,
                constructorQuery)
        );
    }

    @Test
    void findConstructors_whenOneConstructorMatches_returnsConstructor() {
        // GIVEN
        // - expected constructor
        Set<Constructor<?>> expectedConstructors = ImmutableSet.copyOf(OneArgConstructorClass.class.getConstructors());
        // - query to return that constructor
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(OneArgConstructorClass.class)
            .withExactArgTypes(ImmutableList.of(String.class));

        // WHEN
        Set<Constructor<?>> constructors = constructorQuery.findConstructors();

        // THEN
        assertExpectedConstructors(
            expectedConstructors,
            constructors,
            "finding constructors when only one should return",
            constructorQuery);
    }

    @Test
    void findConstructors_whenMultipleConstructorsMatch_returnsMultipleConstructors() {
        // GIVEN
        // expected constructors
        Set<Constructor<?>> expectedConstructors =
            ImmutableSet.copyOf(MultipleConstructorsClass.class.getConstructors());
        // constructor query to return those constructors
        ConstructorQuery constructorQuery = ConstructorQuery.inClass(MultipleConstructorsClass.class);

        // WHEN
        Set<Constructor<?>> constructors = constructorQuery.findConstructors();

        // THEN
        assertExpectedConstructors(
            expectedConstructors,
            constructors,
            "finding when all should return",
            constructorQuery
        );
    }

    private void assertQueryFindsExpectedConstructors(
        ConstructorQuery constructorQuery,
        Set<Constructor<?>> expectedConstructors,
        String whenCondition) {

        Set<Constructor<?>> constructors = constructorQuery.findConstructors();
        assertExpectedConstructors(
            expectedConstructors,
            constructors,
            whenCondition,
            constructorQuery
        );
    }

    private void assertExpectedConstructors(
        Set<Constructor<?>> expectedConstructors,
        Set<Constructor<?>> actualConstructors,
        String whenCondition,
        ConstructorQuery constructorQueryThatWasApplied) {

        assertEquals(
            expectedConstructors,
            actualConstructors,
            String.format(
                "Expected finding constructors (when %s) to return exactly {%s}, but found {%s}.%n" +
                    "Constructor query was: %s",
                whenCondition,
                expectedConstructors,
                actualConstructors,
                constructorQueryThatWasApplied
            )
        );
    }

    private void assertQueryFindsExpectedConstructor(
        ConstructorQuery constructorQuery,
        Constructor<?> expectedConstructor,
        String whenCondition) {

        Set<Constructor<?>> constructors = constructorQuery.findConstructors();
        assertEquals(1,
            constructors.size(),
            String.format(
                "Expected findConstructors (when %s) to return just one constructor (%s), but instead found {%s}.%n" +
                    "Constructor query was: %s",
                whenCondition,
                expectedConstructor,
                constructors,
                constructorQuery)
        );
        Constructor<?> constructor = constructors.iterator().next();
        assertExpectedConstructor(
            expectedConstructor,
            constructor,
            whenCondition,
            constructorQuery
        );
    }

    private void assertExpectedConstructor(
        Constructor<?> expectedConstructor,
        Constructor<?> actualConstructor,
        String whenCondition,
        ConstructorQuery constructorQueryThatWasApplied) {

        assertEquals(
            expectedConstructor,
            actualConstructor,
            String.format(
                "Expected finding constructor (when %s) to return %s, but found %s.%n" +
                    "Constructor query was: %s",
                whenCondition,
                expectedConstructor,
                actualConstructor,
                constructorQueryThatWasApplied)
        );
    }

    private void assertQueryFindsNoConstructors(ConstructorQuery constructorQuery, String whenCondition) {
        Set<Constructor<?>> constructors = constructorQuery.findConstructors();
        assertTrue(
            constructors.isEmpty(),
            String.format(
                "Expected not to find any constructors (when %s), but found {%s}.%nConstructor query was: %s",
                whenCondition,
                constructors,
                constructorQuery)
        );
    }

    private void assertQueryFindsConstructorWithArgTypes(
        ConstructorQuery constructorQuery,
        Class[] argTypes,
        String whenCondition) {

        Set<Constructor<?>> constructors = constructorQuery.findConstructors();
        for (Constructor<?> constructor : constructors) {
            if (Arrays.equals(argTypes, constructor.getParameterTypes())) {
                // found match
                return;
            }
        }

        fail(String.format("Expected query results to include constructor with arg types %s, but only found %s.%n" +
            "Constructor query was: %s",
            Arrays.toString(argTypes),
            constructors,
            constructorQuery)
        );
    }
}
