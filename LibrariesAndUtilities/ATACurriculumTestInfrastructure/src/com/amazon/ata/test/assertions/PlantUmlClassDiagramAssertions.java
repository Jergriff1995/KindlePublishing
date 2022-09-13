package com.amazon.ata.test.assertions;

import com.amazon.ata.test.helper.PlantUmlClassDiagramHelper;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

import static com.amazon.ata.test.assertions.AtaAssertions.assertContains;
import static com.amazon.ata.test.assertions.AtaAssertions.assertMatchesMultiLine;
import static com.amazon.ata.test.assertions.AtaAssertions.assertMatchesSingleLine;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class PlantUmlClassDiagramAssertions {

    private PlantUmlClassDiagramAssertions() {
    }

    /**
     * Asserts that a Plant UML class diagram contains the given substring.
     *
     * @param content The contents of the Plant UML diagram
     * @param expectedString The substring to search for
     */
    public static void assertClassDiagramContains(final String content, final String expectedString) {
        assertContains(
            content,
            expectedString,
            String.format("Expected class diagram to include %s", expectedString)
        );
    }

    /**
     * Asserts that a Plant UML class diagram contains the specified class.
     *
     * @param content The contents of the Plant UML diagram
     * @param className The class name to search for
     */
    public static void assertClassDiagramContainsClass(final String content, final String className) {
        // just in case the type is missing altogether, make sure message makes sense
        assertContains(content, className, "Expected class diagram to include " + className);
        // make sure type is a class; allow:
        //    class <className>
        assertMatchesSingleLine(
            content,
            String.format("class\\s+%s", className),
            String.format("Expected class diagram to show %s as a class", className)
        );
    }

    /**
     * Asserts that a Plant UML class diagram contains the specified enum.
     *
     * @param content The contents of the Plant UML diagram
     * @param enumName The enum name to search for
     */
    public static void assertClassDiagramContainsEnum(final String content, final String enumName) {
        // just in case the type is missing altogether, make sure message makes sense
        assertContains(content, enumName, "Expected class diagram to include " + enumName);
        // make sure type is an enum; allow:
        //    enum <enumName>
        //    class <enumName> <<enumeration>>
        //    enum <enumName> <<enumeration>>
        assertMatchesSingleLine(
            content,
            String.format("(enum.*%s)|(%s.*enum)", enumName, enumName),
            String.format("Expected class diagram to show %s as an enum", enumName)
        );
    }

    /**
     * Asserts that a Plant UML class diagram contains the specified interface.
     *
     * @param content The contents of the Plant UML diagram
     * @param interfaceName The interface name to search for
     */
    public static void assertClassDiagramContainsInterface(final String content, final String interfaceName) {
        // just in case the type is missing altogether, make sure message makes sense
        assertContains(content, interfaceName, "Expected class diagram to include " + interfaceName);


        // make sure type is an interface; allow:
        //    interface <interfaceName>
        //    class <interfaceName> <<interface>>
        //    interface <interfaceName> <<interface>>
        assertContains(content, interfaceName, "Expected class diagram to include " + interfaceName);
        assertMatchesSingleLine(
            content,
            String.format("(interface.*%s)|(%s.*interface)", interfaceName, interfaceName),
            String.format("Expected class diagram to show %s as an interface", interfaceName)
        );
    }

    /**
     * Asserts that a Plant UML class diagram includes a relationship of any kind between the specified classes
     * (represented as {@code String}s).
     *
     * @param content The contents of the Plant UML diagram
     * @param typeOne One side of the relationship (represented as a {@code String}
     * @param typeTwo The other side of the relationship (represented as a {@code String}
     */
    public static void assertClassDiagramIncludesRelationship(final String content,
                                                              final String typeOne,
                                                              final String typeTwo) {
        assertTrue(PlantUmlClassDiagramHelper.classDiagramIncludesRelationship(content, typeOne, typeTwo),
                String.format("Expected diagram to include a relationship between %s and %s", typeOne, typeTwo));
    }

    /**
     * Asserts that a Plant UML class diagram includes a "contains" relationship between the specified classes
     * (represented as {@code String}s).
     *
     * {@code containingType} (the "from" type) is the type where the relationship originates from (it contains {@code
     * containedType}). {@code containedType} (the "to" type) is the type that is contained.
     *
     * @param content The contents of the Plant UML diagram
     * @param containingType The "from" side of the relationship (represented as a {@code String}
     * @param containedType The "to" side of the relationship (represented as a {@code String}
     */
    public static void assertClassDiagramIncludesContainsRelationship(final String content,
                                                                      final String containingType,
                                                                      final String containedType) {
        // report complete absence of relationship if it isn't present
        assertClassDiagramIncludesRelationship(content, containingType, containedType);

        // report incorrect relationship if it's present but incorrect type
        assertTrue(PlantUmlClassDiagramHelper.classDiagramIncludesContainsRelationship(
                content, containingType, containedType),
            String.format(
                "Expected class diagram to include a contains ( e.g. A o-- B OR A *-- B ) relationship " +
                "between %s and %s",
                containingType,
                containedType)
        );
    }

    /**
     * Asserts that a Plant UML class diagram includes a "contains" relationship between the specified classes
     * (represented as instances of {@code Class}).
     *
     * {@code containingType} (the "from" type) is the type where the relationship originates from (it contains {@code
     * containedType}). {@code containedType} (the "to" type) is the type that is contained.
     *
     * @param content The contents of the Plant UML diagram
     * @param containingType The "from" side of the relationship (represented as a {@code Class}
     * @param containedType The "to" side of the relationship (represented as a {@code Class}
     * @param <T> generic type for {@code containingType}'s {@code Class<T>}
     * @param <U> generic type for {@code containedType}'s {@code Class<U>}
     */
    public static <T, U> void assertClassDiagramIncludesContainsRelationship(
            final String content,
            final Class<T> containingType,
            final Class<U> containedType) {
        assertClassDiagramIncludesContainsRelationship(
            content,
            containingType.getSimpleName(),
            containedType.getSimpleName()
        );
    }

    /**
     * Asserts that a Plant UML class diagram includes an "implements" relationship between the specified classes
     * (represented as {@code String}s).
     *
     * {@code implementingType} (the "from" type) is the type where the relationship originates from (it implements
     * {@code implementedInterface}). {@code implementedInterface} (the "to" type) is the interface that is implemented.
     *
     * @param content The contents of the Plant UML diagram
     * @param implementingType The "from" side of the relationship (represented as a {@code String}
     * @param implementedInterface The "to" side of the relationship (represented as a {@code String}
     */
    public static void assertClassDiagramIncludesImplementsRelationship(final String content,
                                                                        final String implementingType,
                                                                        final String implementedInterface) {
        // report complete absence of relationship if it isn't present
        assertClassDiagramIncludesRelationship(content, implementingType, implementedInterface);

        // report incorrect relationship if it's present but incorrect type
        assertMatchesSingleLine(
            content,
            patternForDiagramIncludesImplementsRelationship(implementingType, implementedInterface),
            String.format(
                "Expected class diagram to include an implements ( e.g. A ..|> B ) relationship between %s and %s",
                    implementingType,
                implementedInterface)
        );
    }

    /**
     * Asserts that a Plant UML class diagram includes an "implements" relationship between the specified classes
     * (represented as instances of {@code Class}). (Interfaces are represented by instances of {@code Class}):
     * https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html
     *
     * {@code implementingType} (the "from" type) is the type where the relationship originates from (it implements
     * {@code implementedInterface}). {@code implementedInterface} (the "to" type) is the interface that is implemented.
     *
     * @param content The contents of the Plant UML diagram
     * @param implementingType The "from" side of the relationship (represented as a {@code Class}
     * @param implementedInterface The "to" side of the relationship (represented as a {@code Class}
     * @param <T> generic type for {@code implementingType}'s {@code Class<T>}
     * @param <U> generic type for {@code implementedInterface}'s {@code Class<U>}
     */
    public static <T, U> void assertClassDiagramIncludesImplementsRelationship(
            final String content,
            final Class<T> implementingType,
            final Class<U> implementedInterface) {
        assertClassDiagramIncludesImplementsRelationship(
            content,
            implementingType.getSimpleName(),
            implementedInterface.getSimpleName()
        );
    }

    /**
     * Asserts that a Plant UML class diagram includes an "extends" relationship between the specified classes
     * (represented as {@code String}s).
     *
     * {@code subType} (the "from" type) is the type where the relationship originates from (it extends {@code
     * superType}). {@code superType} (the "to" type) is the type that is extended.
     *
     * @param content The contents of the Plant UML diagram
     * @param subType The "from" side of the relationship (represented as a {@code String}
     * @param superType The "to" side of the relationship (represented as a {@code String}
     */
    public static void assertClassDiagramIncludesExtendsRelationship(final String content,
                                                                     final String subType,
                                                                     final String superType) {
        // report complete absence of relationship if it isn't present
        assertClassDiagramIncludesRelationship(content, subType, superType);

        // report incorrect relationship if it's present but incorrect type
        assertTrue(
                PlantUmlClassDiagramHelper.classDiagramIncludesExtendsRelationship(content, subType, superType),
                String.format(
                    "Expected class diagram to include an extends ( e.g. A --|> B ) relationship " +
                    "between %s and %s",
                    subType,
                    superType)
        );
    }

    /**
     * Asserts that a Plant UML class diagram includes an "extends" relationship between the specified classes
     * (represented as instances of {@code Class}). (Interfaces are represented by instances of {@code Class}):
     * https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html
     *
     * {@code subType} (the "from" type) is the type where the relationship originates from (it extends {@code
     * superType}). {@code superType} (the "to" type) is the type that is extended.
     *
     * @param content The contents of the Plant UML diagram
     * @param subType The "from" side of the relationship (represented as a {@code Class}
     * @param superType The "to" side of the relationship (represented as a {@code Class}
     * @param <T> generic type for {@code subType}'s {@code Class<T>}
     * @param <U> generic type for {@code superType}'s {@code Class<U>}
     */
    public static <T, U> void assertClassDiagramIncludesExtendsRelationship(
            final String content,
            final Class<T> subType,
            final Class<U> superType) {
        assertClassDiagramIncludesExtendsRelationship(content, subType.getSimpleName(), superType.getSimpleName());
    }

    /**
     * Asserts that the class diagram includes the expected member in the specified type (class).
     *
     * @param content The contents of the Plant UML diagram
     * @param type The type/class being inspected
     * @param includedMember The member that we expect to be present in {@code type}. This should probably be
     *                       a variable name, but could also be a type (as long as no other members share this type)
     */
    public static void assertClassDiagramTypeContainsMember(
            final String content,
            final String type,
            final String includedMember) {
        assertClassDiagramTypeContainsMember(content, type, includedMember, includedMember);
    }

    /**
     * Asserts that the class diagram includes the expected member in the specified type (class).
     *
     * @param content The contents of the Plant UML diagram
     * @param type The type/class being inspected
     * @param memberPattern The pattern to look for to verify the member variable is present. May include
     *                      type and/or other annotation/adornment (e.g. @DynamoDBHashKey memberName : String)
     *                      It could also be a type (as long as no other members share this type)
     * @param memberNameForErrorMessaging The name of the expected member (or member variable type),
     *                                    to be used only in error messaging
     */
    public static void assertClassDiagramTypeContainsMember(
            final String content,
            final String type,
            final String memberPattern,
            final String memberNameForErrorMessaging) {
        assertTrue(
            PlantUmlClassDiagramHelper.classDiagramTypeContainsMember(content, type, memberPattern),
            String.format("In class diagram, %s is missing an expected member variable, %s, " +
                          "or %s may declared as an incorrect type",
                          type, memberNameForErrorMessaging, memberNameForErrorMessaging));
    }

    /**
     * Asserts that a class diagram does *not* include the excluded member in the specified type (class).
     *
     * @param content The contents of the Plant UML diagram
     * @param type The type/class being inspected
     * @param excludedMember The member that we expect *not* to be present in {@code type}
     */
    public static void assertClassDiagramTypeDoesNotContainMember(
            final String content,
            final String type,
            final String excludedMember) {
        assertFalse(
                PlantUmlClassDiagramHelper.classDiagramTypeContainsMember(content, type, excludedMember),
                String.format("In class diagram, expected %s to not contain %s, but it did", type, excludedMember));
    }

    /**
     * Asserts that a class diagram has a type that contains the given method, with the
     * given name, arg types, and return type.
     *
     * Does as good of a job of verifying that the method is on the specified type as
     * {@code assertClassDiagramTypeContainsMethod(content, type, methodName)}.
     *
     * Then does a 'best effort' to check the arg types and return type, but it can't guarantee
     * that the method with the given types/return type found isn't actually on another type
     * in the diagram (the regex would be pretty painful).
     *
     * @param content The contents of the Plant UML diagram
     * @param type The type/class being inspected
     * @param methodName The method expected to be in {@code type}
     * @param returnType {@code methodName}'s return type
     * @param argTypes List of {@code methodName}'s arg types
     */
    public static void assertClassDiagramTypeContainsMethod(
            final String content,
            final String type,
            final String methodName,
            final String returnType,
            final List<String> argTypes) {

        // make sure the method exists within the specified type
        assertClassDiagramTypeContainsMethod(content, type, methodName);

        // best effort search for return type if non-null
        if (null != returnType) {
            assertMatchesMultiLine(
                content,
                patternForTypeIncludesMethodReturnType(type, methodName, returnType),
                String.format(
                    "In class diagram, %s's %s method missing expected return type %s",
                    type, methodName, returnType)
            );
        }

        // best effort search for each arg parameter, if any
        if (null != argTypes) {
            for (String argType : argTypes) {
                assertMatchesMultiLine(
                    content,
                    patternForTypeIncludesMethodAndAnArgType(type, methodName, argType),
                    String.format(
                        "In class diagram, %s's %s method missing expected arg type %s",
                        type, methodName, argType)
                );
            }
        }
    }

    /**
     * Asserts that a class diagram *does* include the expected method in the specified type (class).
     * Just checks for {@code methodName(} inside the specified type.
     *
     * @param content The contents of the Plant UML diagram
     * @param type The type/class being inspected
     * @param methodName The method expected to be in {@code type}
     */
    public static void assertClassDiagramTypeContainsMethod(
            final String content,
            final String type,
            final String methodName) {
        assertMatchesMultiLine(
            content,
            patternForTypeIncludesMethodNameDeclared(type, methodName),
            String.format("In class diagram, %s is missing an expected method, %s", type, methodName));
    }



    private static String patternForDiagramIncludesImplementsRelationship(
            final String implementingType, final String implementedInterface) {
        // allow the implements relationship to be in either direction, but require the closed arrow shape: |>
        List<String> validPatterns = ImmutableList.of(
            // implementingType ..|> implementedInterface
            String.format("(%s.*\\.+\\|>.*%s)", implementingType, implementedInterface),
            // implementedInterface <|.. implementingType
            String.format("(%s.*<\\|\\.+.*%s)", implementedInterface, implementingType)
        );

        return validPatterns.stream().collect(Collectors.joining("|"));
    }

    private static String patternForTypeIncludesMethodReturnType(
            final String type,
            final String methodName,
            final String returnType) {

        // sometime after type declared:

        // methodName(...): ReturnType
        // methodName(...)  :  ReturnType
        // methodName (...): ReturnType
        // methodName (...)  :  ReturnType
        return String.format("%s[^{]*\\{[^}]*%s\\s*\\([^)]*\\)\\s*:\\s*%s", type, methodName, returnType);
    }

    private static String patternForTypeIncludesMethodAndAnArgType(
            final String type,
            final String methodName,
            final String anArgType) {

        // sometime after type declared:

        // methodName (AnArgType)
        // methodName ( AnArgType)
        // methodName (AnArgType varname)
        // methodName (varname : AnArgType)
        // methodName (varname : AnArgType, <other args>)
        // methodName (AnArgType varname, <other args>)
        // methodName (<other args>, AnArgType, <more args>)
        return String.format("%s[^{]*\\{.*%s\\s*\\([^)]*%s[^)]*\\)", type, methodName, anArgType);
    }

    private static String patternForTypeIncludesMethodNameDeclared(final String type, final String method) {
        // class/enum/interface TypeOne {
        //     ...
        //     + { abstract } expectedMethod(args) : ReturnType
        //     +{static}expectedMethod(args) : ReturnType
        return String.format(
            "%s[^{]*\\{(?:\\{\\s*static\\s*\\}|\\{\\s*abstract\\s*\\}|[^}])*%s\\s*\\(",
            type,
            method);
    }
}
