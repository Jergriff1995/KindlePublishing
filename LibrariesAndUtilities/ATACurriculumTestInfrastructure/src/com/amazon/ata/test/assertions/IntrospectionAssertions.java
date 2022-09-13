package com.amazon.ata.test.assertions;

import org.mockito.Mock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Contains assertions tied to introspecting the class structure of the project, for example which classes exist,
 * whether a class has specific member methods or variables.
 */
public final class IntrospectionAssertions {

    private IntrospectionAssertions() {
    }

    /**
     * Verifies that one Class directly subclasses from/extends the other.
     *
     * @param subclass The class expected to subclass {@code expectedSuperclass}
     * @param expectedSuperclass The class expected to be subclassed by {@code subclass}
     */
    public static void assertDirectlyExtends(final Class<?> subclass, final Class<?> expectedSuperclass) {
        Class<?> actualSuperclass = subclass.getSuperclass();
        assertEquals(
            expectedSuperclass,
            actualSuperclass,
            String.format("Unexpected class hierarchy for class %s", subclass.getSimpleName()));
    }

    /**
     * Verifies that {@code subtype} class implements the {@code implementedInterface} interface. Will
     * {@code fail()} if not.
     *
     * Will also {@code fail()} if {@code subtype} is an interface or if {@code implementedInterface}
     * is *not* an interface.
     *
     * @param subtype Class to check to see if it implements {@code implementedInterface}
     * @param implementedInterface Interface to check to see if {@code subtype} implements it
     */
    public static void assertImplementsInterface(final Class<?> subtype, final Class<?> implementedInterface) {
        assertTrue(
            implementedInterface.isInterface(),
            "Expected implementedInterface argument to be an interface but was " + implementedInterface.getSimpleName()
        );
        assertFalse(
            subtype.isInterface(),
            "Expected subtype argument to NOT be an interface but was " + subtype.getSimpleName()
        );

        assertTrue(
            implementedInterface.isAssignableFrom(subtype),
            String.format(
                "Expected %s to implement %s interface, but it does not appear to",
                subtype.getSimpleName(),
                implementedInterface.getSimpleName())
        );
    }

    /**
     * Verifies that {@code subtype} class does NOT implement the {@code notImplementedInterface} interface.
     * Will {@code fail()} if it does implement the interface.
     *
     * Will also {@code fail()} if {@code subtype} is an interface itself, or if {@code notImplementedInterface}
     * is *not* actually an interface.
     * @param subtype Class to check to verify that it does not implement the provided interface
     * @param notImplementedInterface Interface to check to verify that {@code subtype} does not implement it
     */
    public static void assertDoesNotImplementInterface(final Class<?> subtype, final Class<?> notImplementedInterface) {
        assertTrue(
            notImplementedInterface.isInterface(),
            "Expected notImplementedInterface argument to be an interface but was " +
            notImplementedInterface.getSimpleName()
        );
        assertFalse(
            subtype.isInterface(),
            "Expected subtype argument to NOT be an interface but was " + subtype.getSimpleName()
        );

        assertFalse(
            notImplementedInterface.isAssignableFrom(subtype),
            String.format(
                "Expected %s to NOT implement %s interface, but it does appear to",
                subtype.getSimpleName(),
                notImplementedInterface.getSimpleName())
        );
    }

    /**
     * Verifies that the given class has the set of member variable types specified (as {@code String}s). The class
     * may contain additional member variables beyond the set being verified and not cause assertion failure.
     *
     * Order of type names in {@code expectedTypeNames} does not matter. If a class is expected to have
     * multiple member variables of the same type, there should be an entry for each member variable of
     * that type in {@code expectedTypeNames}.
     *
     * Type names will exclude generic parameters for generic types. Non-primitive types must be fully
     * qualified class names
     *
     * @param clazz The class to introspect, looking for expected member variables
     * @param expectedTypeNames The types for each of the member variables, expressed as primitives (e.g. "int") or
     *                          fully qualified class names
     */
    public static void assertClassContainsMemberVariableTypes(
            final Class<?> clazz,
            final String[] expectedTypeNames) {

        List<String> foundTypeNames = Arrays.stream(clazz.getDeclaredFields())
            .map(Field::getType)
            .map(Type::getTypeName)
            .collect(Collectors.toList());

        for (String expectedTypeName: expectedTypeNames) {
            assertTrue(foundTypeNames.contains(expectedTypeName),
                clazz.getSimpleName() + " is missing an expected member variable of type " + expectedTypeName);
            foundTypeNames.remove(expectedTypeName);
        }
    }

    /**
     * Verifies that the given class has the expected member methods specified by name.
     *
     * Order of the method names does not matter. If methods are overloaded, there should be an entry for each
     * overloaded version of the method in {@code expectedMethodNames}.
     *
     * @param clazz The class to introspect, looking for expected methods
     * @param expectedMethodNames The method names expected to be found in {@code clazz}
     */
    public static void assertClassContainsMemberMethodNames(final Class<?> clazz, final String[] expectedMethodNames) {
        List<String> foundMethodNames = Arrays.stream(clazz.getMethods())
            .map(Method::getName)
            .collect(Collectors.toList());

        for (String expectedMethodName : expectedMethodNames) {
            assertTrue(foundMethodNames.contains(expectedMethodName),
                clazz.getSimpleName() + " is missing an expected method");
            foundMethodNames.remove(expectedMethodName);
        }
    }

    /**
     * Verifies that the given class *does not* have any of the given member methods specified by name, and will
     * fail if any of the unexpected method names is found in the class.
     *
     * Order of the method names does not matter.
     *
     * @param clazz The class to introspect, looking for unexpected methods
     * @param unexpectedMethodNames The method names *not* expected to be found in {@code clazz}
     */
    public static void assertClassDoesNotContainMemberMethodNames(
            final Class<?> clazz,
            final String[] unexpectedMethodNames) {
        List<String> foundMethodNames = Arrays.stream(clazz.getMethods())
            .map(Method::getName)
            .collect(Collectors.toList());

        for (String unexpectedMethodName : unexpectedMethodNames) {
            assertFalse(foundMethodNames.contains(unexpectedMethodName),
                clazz.getSimpleName() + " contains an unexpected method: " + unexpectedMethodName);
        }
    }

    /**
     * Verifies that the given field from the given test class is mocked (via {@code @Mock} annotation).
     *
     * Will {@code fail()} if {@code fieldOnTest} is {@code null}, or if {@code fieldOnTest}
     * does not have a {@code @Mock} annotation. Returns true otherwise.
     *
     * @param fieldOnTest The field from a test class to inspect for mocking
     * @param testClass The class the field came from (in case {@code fieldOnTest} is {@code null}
     * @param memberType {@code fieldOnTest}'s type (in case {@code fieldOnTest} is {@code null}
     */
    public static void assertMemberMocked(
            final Field fieldOnTest, final Class<?> testClass, final Class<?> memberType) {

        if (null == fieldOnTest) {
            fail(String.format(
                "Could not find a %s member on %s",
                memberType.getSimpleName(),
                testClass.getSimpleName())
            );
        } else {
            Annotation[] annotations = fieldOnTest.getAnnotations();
            assertTrue(
                annotationsIncludeMock(annotations),
                String.format(
                    "%s has instance variable of type %s, but it appears that it may not be mocked? " +
                        "Annotations for member: %s",
                    testClass.getSimpleName(),
                    memberType.getSimpleName(),
                    Arrays.toString(annotations))
            );
        }
    }

    /**
     * Indicates if the given set of annotations includes {@code @Mock}.
     *
     * @param annotations The array of annotations to inspect
     * @return {@code true} if {@code @Mock} found, {@code false} otherwise
     */
    public static boolean annotationsIncludeMock(final Annotation[] annotations) {
        boolean foundMockAnnotation = false;

        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Mock.class)) {
                foundMockAnnotation = true;
            }
        }

        return foundMockAnnotation;
    }
}
