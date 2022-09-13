package com.amazon.ata.test.assertions;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentest4j.AssertionFailedError;

import java.util.List;

import static com.amazon.ata.test.assertions.AtaAssertions.assertContains;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class PlantUmlClassDiagramAssertionsTest {
    private static final String RIGHT_SIMPLE = "class One {} \nClassTwo {} \nClassOne -- ClassTwo ";
    private static final String LEFT_SIMPLE = "class One {} \nClassTwo {} \nClassTwo <-- ClassOne ";
    private static final String RIGHT_AGGREGATION = "class One {} \nClassTwo {} \nClassOne o--> ClassTwo ";
    private static final String LEFT_AGGREGATION = "class One {} \nClassTwo {} \nClassTwo --o ClassOne ";
    private static final String RIGHT_COMPOSITION = "class One {} \nClassTwo {} \nClassOne *-- ClassTwo ";
    private static final String LEFT_COMPOSITION = "class One {} \nClassTwo {} \nClassTwo <--* ClassOne ";
    private static final String RIGHT_IMPLEMENTS = "class One {} \nClassTwo {} \nClassOne ..|> ClassTwo ";
    private static final String LEFT_IMPLEMENTS = "class One {} \nClassTwo {} \nClassTwo <|.. ClassOne ";
    private static final String RIGHT_EXTENDS = "class One {} \nClassTwo {} \nClassOne --|> ClassTwo ";
    private static final String LEFT_EXTENDS = "class One {} \nClassTwo {} \nClassTwo <|-- ClassOne ";
    private static final String CLASS_ONE = "class ClassOne {\n  -member1 : String\n+getMember1() : String\n}\n";
    private static final String TWO_CLASSES = "class ClassOne {\n -member1 : String\n+getMember1() : String\n}\n" +
                                              "class ClassTwo{\n -member2 : String\n+getMember2() : String\n}\n";
    private static final String CLASS_WITH_ABSTRACT_METHOD =
            "class Abstracty {\n + { abstract}implementMe() : int\n}\n";
    private static final String CLASS_WITH_STATIC_METHOD = "class Staticky {\n  +{static}classMethod() : String\n}\n";
    private static final String INTERFACE_WITH_METHOD = "interface Quotable<<interface>> {\nquote(String) : Quote\n}";
    private static final String CLASS_WITH_METHOD =
            "class ClassOne{\nmethodOne(varname: ArgType) : ReturnType\n}\nclass OtherClass {}";
    private static final String CLASS_WITH_METHOD_NO_RETURN =
            "class ClassOne{\nmethodOne(varname: ArgType)\n}\nclass OtherClass {}";
    private static final String CLASS_WITH_METHOD_NO_ARG =
            "class ClassOne{\nmethodOne( ) : ReturnType\n}\nclass OtherClass {}";

    // For the diagram methods that accept Class args
    private static class ClassOne {}
    private static class ClassTwo {}
    private static class ClassThree {}

    // assertClassDiagramContains

    @Test
    void assertClassDiagramContains_stringMatches_noAssertFires() {
        // GIVEN -- matching string
        String matchingClass = "Automobile";

        // WHEN + THEN -- no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramContains(
                "class Automobile {\n    +getTires() : List<Tire>\n}", matchingClass);
    }

    @Test
    void assertClassDiagramContains_stringDoesntMatch_assertFires() {
        // GIVEN -- mon-matching string
        String nonMatchingClass = "Taxi";

        // GIVEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramContains(
                        "class Automobile {\n    +getTires() : " + "List<Tire>\n}",
                        nonMatchingClass)
        );
    }

    // assertClassDiagramContainsClass

    @Test
    void assertClassDiagramContainsClass_classExists_noAssertFires() {
        // GIVEN - diagram with class
        // matching class name
        String className = "ClassOne";

        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramContainsClass(CLASS_ONE, className);
    }

    @Test
    void assertClassDiagramContainsClass_classDoesntExist_assertFires() {
        // GIVEN - diagram without requested class
        // non-matching class name
        String className = "ClassTwo";

        // WHEN + THEN - assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramContainsClass(CLASS_ONE, className)
        );
    }

    // assertClassDiagramContainsEnum

    @Test
    void assertClassDiagramContainsEnum_enumExistsWithEnumDeclaration_noAssertFires() {
        // GIVEN - diagram with requested enum
        String diagram = "enum EnumOne {\nONE, TWO, THREE\n}";
        // matching enum name
        String enumName = "EnumOne";

        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramContainsEnum(diagram, enumName);
    }

    @Test
    void assertClassDiagramContainsEnum_enumExistsWithAngleBracketAnnotation_noAssertFires() {
        // GIVEN - diagram with requested enum
        String diagram = "class EnumOne <<enumeration>> {\nONE, TWO, THREE\n}";
        // matching enum name
        String enumName = "EnumOne";

        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramContainsEnum(diagram, enumName);
    }

    @Test
    void assertClassDiagramContainsEnum_enumExistsWithEnumAndAngleBracketAnnotation_noAssertFires() {
        // GIVEN - diagram with requested enum
        String diagram = "enum EnumOne <<enumeration>> {\nONE, TWO, THREE\n}";
        // matching enum name
        String enumName = "EnumOne";

        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramContainsEnum(diagram, enumName);
    }

    @Test
    void assertClassDiagramContainsEnum_enumDoesntExist_assertFires() {
        // GIVEN - diagram without requested enum
        String diagram = "enum EnumOne <<enumeration>> {\nONE, TWO, THREE\n}";
        // non-matching enum name
        String enumName = "EnumTwo";

        // WHEN + THEN - assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramContainsEnum(diagram, enumName)
        );
    }

    // assertClassDiagramContainsInterface

    @Test
    void assertClassDiagramContainsInterface_interfaceExistsWithInterfaceDeclaration_noAssertFires() {
        // GIVEN - diagram with requested interface
        String diagram = "interface InterfaceOne {\nvoid methodOne();\n}";
        // matching interface name
        String interfaceName = "InterfaceOne";

        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramContainsInterface(diagram, interfaceName);
    }

    @Test
    void assertClassDiagramContainsInterface_interfaceExistsWithAngleBracketAnnotation_noAssertFires() {
        // GIVEN - diagram with requested interface
        String diagram = "class InterfaceOne <<interface>> {\nvoid methodOne();\n}";
        // matching interface name
        String interfaceName = "InterfaceOne";

        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramContainsInterface(diagram, interfaceName);
    }

    @Test
    void assertClassDiagramContainsInterface_interfaceExistsWithInterfaceAndAngleBracketAnnotation_noAssertFires() {
        // GIVEN - diagram with requested interface
        String diagram = "interface InterfaceOne <<interface>> {\nvoid methodOne();\n}";
        // matching interface name
        String interfaceName = "InterfaceOne";

        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramContainsInterface(diagram, interfaceName);
    }

    @Test
    void assertClassDiagramContainsInterface_interfaceDoesntExist_assertFires() {
        // GIVEN - diagram without requested interface
        String diagram = "interface InterfaceOne <<interface>> {\nvoid methodOne();\n}";
        // non-matching interface name
        String interfaceName = "InterfaceTwo";

        // WHEN + THEN - assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramContainsInterface(diagram, interfaceName)
        );

    }

    // assertClassDiagramIncludesRelationship

    @ParameterizedTest
    @ValueSource(strings = {
            RIGHT_SIMPLE, LEFT_SIMPLE,
            RIGHT_COMPOSITION, LEFT_COMPOSITION,
            RIGHT_AGGREGATION, LEFT_AGGREGATION,
            RIGHT_IMPLEMENTS, LEFT_IMPLEMENTS,
            RIGHT_EXTENDS, LEFT_EXTENDS})
    void assertClassDiagramIncludesRelationship_relationshipExists_noAssertFires(String diagramIncludingRelationship) {
        // GIVEN -- diagram with relationship from ClassOne to ClassTwo
        // WHEN + THEN -- no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramIncludesRelationship(
                diagramIncludingRelationship, "ClassOne", "ClassTwo"
        );
    }

    @Test
    void assertClassDiagramIncludesRelationship_commaDelimitedListOfClasses_assertFires() {
        // GIVEN -- diagram with classes in a comma-delimited list
        String diagram = "ClassOne, ClassTwo";

        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesRelationship(
                        diagram, "ClassOne", "ClassTwo")
        );
    }

    @Test
    void assertClassDiagramIncludesRelationship_oneClassButNotTheOther_assertFires() {
        // GIVEN -- diagram with relationship including one class but not the other
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesRelationship(
                        RIGHT_COMPOSITION, "ClassOne", "ClassSix")
        );
    }

    @Test
    void assertClassDiagramIncludesRelationship_classesHaveRelationshipsWithOtherClasses_assertFires() {
        // GIVEN -- two relationships among three classes, one pairing is excluded
        String associations = "ClassOne -- ClassTwo\nClassTwo -- ClassThree";

        // WHEN + THEN
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesRelationship(
                        associations, "ClassOne", "ClassThree")
        );
    }

    // assertClassDiagramIncludesContainsRelationship(String, String)

    @ParameterizedTest
    @ValueSource(strings = {
            RIGHT_SIMPLE, LEFT_SIMPLE,
            RIGHT_COMPOSITION, LEFT_COMPOSITION,
            RIGHT_AGGREGATION, LEFT_AGGREGATION})
    void assertClassDiagramIncludesContainsRelationship_relationshipExists_noAssertFires(String diagram) {
        // GIVEN -- diagram with a valid relationship in the correct direction
        // WHEN + THEN -- no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramIncludesContainsRelationship(
                diagram, "ClassOne", "ClassTwo"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {RIGHT_COMPOSITION, RIGHT_AGGREGATION})
    void assertClassDiagramIncludesContainsRelationship_String_reversedRelationship_assertFires(String diagram) {
        // GIVEN -- diagram with appropriate relationship, but reversed
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesContainsRelationship(
                        diagram, "ClassTwo", "ClassOne")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {RIGHT_IMPLEMENTS, RIGHT_EXTENDS})
    void assertClassDiagramIncludesContainsRelationship_String_wrongRelationshipType_assertFires(String diagram) {
        // GIVEN -- diagram with relationship of wrong type but in correct direction
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesContainsRelationship(
                        diagram, "ClassOne", "ClassTwo")
        );
    }

    @Test
    void assertClassDiagramIncludesContainsRelationship_String_noRelationship_assertFiresWithRelationshipMissingMsg() {
        // GIVEN -- message indicating no relationship exists at all
        String expectedMessage = "Expected diagram to include a relationship between";

        // WHEN
        try {
            PlantUmlClassDiagramAssertions.assertClassDiagramIncludesContainsRelationship(
                    RIGHT_SIMPLE, "ClassOne", "ClassThree"
            );
            // THEN -- assert failure with appropriate message
        } catch (AssertionFailedError e) {
            assertContains(
                    e.getMessage(),
                    expectedMessage,
                    String.format("Expected assertion message ('%s') to include '%s', but didn't",
                                  e.getMessage(),
                                  expectedMessage)
            );

            // test pass
            return;
        }
        fail("Expected assertion failure!");
    }

    // assertClassDiagramIncludesContainsRelationship(Class, Class)

    @ParameterizedTest
    @ValueSource(strings = {
            RIGHT_SIMPLE, LEFT_SIMPLE,
            RIGHT_COMPOSITION, LEFT_COMPOSITION,
            RIGHT_AGGREGATION, LEFT_AGGREGATION})
    void assertClassDiagramIncludesContainsRelationship_Class_relationshipExists_noAssertFires(String diagram) {
        // GIVEN -- diagram with a valid relationship in the correct direction
        // WHEN + THEN -- no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramIncludesContainsRelationship(
                diagram,
                ClassOne.class,
                ClassTwo.class
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {RIGHT_COMPOSITION, RIGHT_AGGREGATION})
    void assertClassDiagramIncludesContainsRelationship_Class_reversedRelationship_assertFires(String diagram) {
        // GIVEN -- diagram with appropriate relationship, but reversed
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesContainsRelationship(
                        diagram,
                        ClassTwo.class,
                        ClassOne.class)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {RIGHT_IMPLEMENTS, RIGHT_EXTENDS})
    void assertClassDiagramIncludesContainsRelationship_Class_wrongRelationshipType_assertFires(String diagram) {
        // GIVEN -- diagram with relationship of wrong type but in correct direction
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesContainsRelationship(
                        diagram,
                        ClassOne.class,
                        ClassTwo.class)
        );
    }

    @Test
    void assertClassDiagramIncludesContainsRelationship_Class_noRelationship_assertFiresWithRelationshipMissingMsg() {
        // GIVEN -- message indicating no relationship exists at all
        String expectedMessage = "Expected diagram to include a relationship between";

        // WHEN
        try {
            PlantUmlClassDiagramAssertions.assertClassDiagramIncludesContainsRelationship(
                    RIGHT_SIMPLE,
                    ClassOne.class,
                    ClassThree.class
            );
            // THEN -- assert failure with appropriate message
        } catch (AssertionFailedError e) {
            assertContains(
                    e.getMessage(),
                    expectedMessage,
                    String.format("Expected assertion message ('%s') to include '%s', but didn't",
                                  e.getMessage(),
                                  expectedMessage)
            );

            // test pass
            return;
        }
        fail("Expected assertion failure!");
    }

    // assertClassDiagramIncludesImplementsRelationship(String, String)

    @ParameterizedTest
    @ValueSource(strings = {RIGHT_IMPLEMENTS, LEFT_IMPLEMENTS})
    void assertClassDiagramIncludesImplementsRelationship_String_implements_noAssertFires(String diagram) {
        // GIVEN -- diagram with implements relationship that matches
        // WHEN + THEN -- no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramIncludesImplementsRelationship(
                diagram, "ClassOne", "ClassTwo"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            RIGHT_COMPOSITION, LEFT_COMPOSITION,
            RIGHT_AGGREGATION, LEFT_AGGREGATION,
            RIGHT_EXTENDS, LEFT_EXTENDS})
    void assertClassDiagramIncludesImplementsRelationship_String_wrongRelationshipType_assertFires(String diagram) {
        // GIVEN -- diagram with relationship in correct direction between correct types, but write relationship type
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesImplementsRelationship(
                        diagram, "ClassOne", "ClassTwo")
        );
    }

    @Test
    void assertClassDiagramIncludesImplementsRelationship_String_implementsReversed_assertFires() {
        // GIVEN -- implements relationship, but in wrong direction
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesImplementsRelationship(
                        RIGHT_IMPLEMENTS, "ClassTwo", "ClassOne")
        );
    }

    @Test
    void assertClassDiagramIncludesImplementsRelationship_String_noRelationship_assertFiresWithRelationshipMissingMsg() {
        // GIVEN -- message indicating no relationship exists at all
        String expectedMessage = "Expected diagram to include a relationship between";

        // WHEN
        try {
            PlantUmlClassDiagramAssertions.assertClassDiagramIncludesImplementsRelationship(
                    RIGHT_IMPLEMENTS, "ClassOne", "ClassThree"
            );
            // THEN -- assert failure with appropriate message
        } catch (AssertionFailedError e) {
            assertContains(
                    e.getMessage(),
                    expectedMessage,
                    String.format("Expected assertion message ('%s') to include '%s', but didn't",
                                  e.getMessage(),
                                  expectedMessage)
            );

            // test pass
            return;
        }
        fail("Expected assertion failure!");
    }

    // assertClassDiagramIncludesImplementsRelationship(Class, Class)

    @ParameterizedTest
    @ValueSource(strings = {RIGHT_IMPLEMENTS, LEFT_IMPLEMENTS})
    void assertClassDiagramIncludesImplementsRelationship_Class_implements_noAssertFires(String diagram) {
        // GIVEN -- diagram with implements relationship that matches
        // WHEN + THEN -- no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramIncludesImplementsRelationship(
                diagram,
                ClassOne.class,
                ClassTwo.class
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            RIGHT_COMPOSITION, LEFT_COMPOSITION,
            RIGHT_AGGREGATION, LEFT_AGGREGATION,
            RIGHT_EXTENDS, LEFT_EXTENDS})
    void assertClassDiagramIncludesImplementsRelationship_Class_wrongRelationshipType_assertFires(String diagram) {
        // GIVEN -- diagram with relationship in correct direction between correct types, but write relationship type
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesImplementsRelationship(
                        diagram,
                        ClassOne.class,
                        ClassTwo.class)
        );
    }

    @Test
    void assertClassDiagramIncludesImplementsRelationship_Class_implementsReversed_assertFires() {
        // GIVEN -- implements relationship, but in wrong direction
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesImplementsRelationship(
                        RIGHT_IMPLEMENTS,
                        ClassTwo.class,
                        ClassOne.class)
        );
    }

    @Test
    void assertClassDiagramIncludesImplementsRelationship_Class_noRelationship_assertFiresWithRelationshipMissingMsg() {
        // GIVEN -- message indicating no relationship exists at all
        String expectedMessage = "Expected diagram to include a relationship between";

        // WHEN
        try {
            PlantUmlClassDiagramAssertions.assertClassDiagramIncludesImplementsRelationship(
                    RIGHT_IMPLEMENTS,
                    ClassOne.class,
                    ClassThree.class
            );
            // THEN -- assert failure with appropriate message
        } catch (AssertionFailedError e) {
            assertContains(
                    e.getMessage(),
                    expectedMessage,
                    String.format("Expected assertion message ('%s') to include '%s', but didn't",
                                  e.getMessage(),
                                  expectedMessage)
            );

            // test pass
            return;
        }
        fail("Expected assertion failure!");
    }

    // assertClassDiagramIncludesExtendsRelationship(String, String)

    @ParameterizedTest
    @ValueSource(strings = {RIGHT_EXTENDS, LEFT_EXTENDS})
    void assertClassDiagramIncludesExtendsRelationship_String_extends_noAssertFires(String diagram) {
        // GIVEN -- diagram with extends relationship that matches
        // WHEN + THEN -- no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramIncludesExtendsRelationship(
                diagram,"ClassOne", "ClassTwo"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            RIGHT_COMPOSITION, LEFT_COMPOSITION,
            RIGHT_AGGREGATION, LEFT_AGGREGATION,
            RIGHT_IMPLEMENTS, LEFT_IMPLEMENTS})
    void assertClassDiagramIncludesExtendsRelationship_String_wrongRelationshipType_assertFires(String diagram) {
        // GIVEN -- diagram with relationship in correct direction between correct types, but write relationship type
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesExtendsRelationship(
                        diagram, "ClassOne", "ClassTwo")
        );
    }

    @Test
    void assertClassDiagramIncludesExtendsRelationship_String_extendsReversed_assertFires() {
        // GIVEN -- extends relationship, but in wrong direction
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesExtendsRelationship(
                        RIGHT_EXTENDS, "ClassTwo", "ClassOne")
        );
    }

    @Test
    void assertClassDiagramIncludesExtendsRelationship_String_noRelationship_assertFiresWithRelationshipMissingMsg() {
        // GIVEN -- message indicating no relationship exists at all
        String expectedMessage = "Expected diagram to include a relationship between";

        // WHEN
        try {
            PlantUmlClassDiagramAssertions.assertClassDiagramIncludesExtendsRelationship(
                    RIGHT_EXTENDS, "ClassOne", "ClassThree"
            );
            // THEN -- assert failure with appropriate message
        } catch (AssertionFailedError e) {
            assertContains(
                    e.getMessage(),
                    expectedMessage,
                    String.format("Expected assertion message ('%s') to include '%s', but didn't",
                                  e.getMessage(),
                                  expectedMessage)
            );

            // test pass
            return;
        }
        fail("Expected assertion failure!");
    }

    // assertClassDiagramIncludesExtendsRelationship(Class, Class)

    @ParameterizedTest
    @ValueSource(strings = {RIGHT_EXTENDS, LEFT_EXTENDS})
    void assertClassDiagramIncludesExtendsRelationship_Class_extends_noAssertFires(String diagram) {
        // GIVEN -- diagram with extends relationship that matches
        // WHEN + THEN -- no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramIncludesExtendsRelationship(
                diagram,
                ClassOne.class,
                ClassTwo.class
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            RIGHT_COMPOSITION, LEFT_COMPOSITION,
            RIGHT_AGGREGATION, LEFT_AGGREGATION,
            RIGHT_IMPLEMENTS, LEFT_IMPLEMENTS})
    void assertClassDiagramIncludesExtendsRelationship_Class_wrongRelationshipType_assertFires(String diagram) {
        // GIVEN -- diagram with relationship in correct direction between correct types, but write relationship type
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesExtendsRelationship(
                        diagram,
                        ClassOne.class,
                        ClassTwo.class)
        );
    }

    @Test
    void assertClassDiagramIncludesExtendsRelationship_Class_extendsReversed_assertFires() {
        // GIVEN -- extends relationship, but in wrong direction
        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramIncludesExtendsRelationship(
                        RIGHT_EXTENDS,
                        ClassTwo.class,
                        ClassOne.class)
        );
    }

    @Test
    void assertClassDiagramIncludesExtendsRelationship_Class_noRelationship_assertFiresWithRelationshipMissingMsg() {
        // GIVEN -- message indicating no relationship exists at all
        String expectedMessage = "Expected diagram to include a relationship between";

        // WHEN
        try {
            PlantUmlClassDiagramAssertions.assertClassDiagramIncludesExtendsRelationship(
                    RIGHT_EXTENDS,
                    ClassOne.class,
                    ClassThree.class
            );
            // THEN -- assert failure with appropriate message
        } catch (AssertionFailedError e) {
            assertContains(
                    e.getMessage(),
                    expectedMessage,
                    String.format("Expected assertion message ('%s') to include '%s', but didn't",
                                  e.getMessage(),
                                  expectedMessage)
            );

            // test pass
            return;
        }
        fail("Expected assertion failure!");
    }

    // assertClassDiagramTypeContainsMember(String, String, String)

    @Test
    void assertClassDiagramTypeContainsMember_memberIsContained_noAssertFires() {
        // GIVEN -- single class with single member
        // WHEN + THEN - that member is found, no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMember(
                CLASS_ONE, "ClassOne", "member1"
        );
    }

    @Test
    void assertClassDiagramTypeContainsMember_memberNotContained_assertFires() {
        // GIVEN -- single class with single member
        // WHEN + THEN - nonmember is not found - assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMember(
                        CLASS_ONE, "ClassOne", "notInHere")
        );
    }

    @Test
    void assertClassDiagramTypeContainsMember_memberExistsInDifferentClass_assertFires() {
        // GIVEN -- two classes
        // WHEN + THEN - don't find one class's member in the other - assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMember(
                        TWO_CLASSES, "ClassOne", "member2")
        );
    }

    // assertClassDiagramTypeContainsMember(String, String, String, String)

    @Test
    void assertClassDiagramTypeContainsMember_withOptionalParameter_memberIsContained_noAssertFires() {
        // GIVEN -- single class with single member
        // WHEN + THEN - that member is found, no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMember(
            CLASS_ONE, "ClassOne", "member1\\s*:\\s*String", "member1"
        );
    }

    @Test
    void assertClassDiagramTypeContainsMember_withOptionalParameter_memberNotContained_assertFires() {
        // GIVEN -- single class with single member
        // WHEN + THEN - nonmember is not found - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMember(
                CLASS_ONE, "ClassOne", "notInHere\\s*:\\s*Integer", "notInHere")
        );
    }

    @Test
    void assertClassDiagramTypeContainsMember_withOptionalParameter_memberExistsInDifferentClass_assertFires() {
        // GIVEN -- two classes
        // WHEN + THEN - don't find one class's member in the other - assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMember(
                TWO_CLASSES, "ClassOne", "member2: BigDecimal", "member2")
        );
    }

    // assertClassDiagramTypeDoesNotContainMember

    @Test
    void assertClassDiagramTypeDoesNotContainMember_memberNotContained_noAssertFires() {
        // GIVEN -- single class with single member
        // WHEN + THEN - nonmember is not found - assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeDoesNotContainMember(
                CLASS_ONE, "ClassOne", "notInHere"
        );
    }

    @Test
    void assertClassDiagramTypeDoesNotContainMember_memberExistsInDifferentClass_noAssertFires() {
        // GIVEN -- two classes
        // WHEN + THEN - don't find one class's member in the other - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeDoesNotContainMember(
                TWO_CLASSES, "ClassOne", "member2"
        );
    }

    @Test
    void assertClassDiagramTypeDoesNotContainMember_memberIsContained_assertFires() {
        // GIVEN -- single class with single member
        // WHEN + THEN - that member is found, no assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeDoesNotContainMember(
                        CLASS_ONE, "ClassOne", "member1")
        );
    }

    // assertClassDiagramTypeContainsMethod(content, type, methodName, returnType, argTypes)

    @ParameterizedTest
    @ValueSource(strings = {
            "class ClassOne {\nmethodOne()\n}",
            "class ClassOne {\nmethodOne(ArgType varname)\n}",
            "class ClassOne {\nmethodOne(varname: ArgType)\n}",
            "class ClassOne {\nmethodOne(String one, ArgType varname, String two)\n}",
            "class ClassOne {\nmethodOne(String, ArgType, String)\n}",
            "class ClassOne {\nmethodOne(one : String, varname: ArgType, two: String)\n}",
            "class ClassOne {\nanotherMethod(): void\nmethodOne()\n}"
    })
    void
    assertClassDiagramTypeContainsMethod_methodNameOnly_noAssertFires(String diagram) {
        // GIVEN - single class with expected method, with/without args/return type
        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                diagram, "ClassOne", "methodOne", null, null);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "class ClassOne {\nmethodOne(): ReturnType\n}",
            "class ClassOne {\nmethodOne() : ReturnType\n}",
            "class ClassOne {\nmethodOne(ArgType varname) : ReturnType\n}",
            "class ClassOne {\nmethodOne(varname: ArgType):ReturnType\n}",
            "class ClassOne {\nmethodOne(String one, ArgType varname, String two): ReturnType\n}",
            "class ClassOne {\nmethodOne(String, ArgType, String) : ReturnType\n}",
            "class ClassOne {\nmethodOne(one : String, varname: ArgType, two: String) : ReturnType\n}",
            "class ClassOne {\nanotherMethod(): void\nmethodOne(): ReturnType\n}"
    })
    void
    assertClassDiagramTypeContainsMethod_methodNameAndReturnType_noAssertFires(String diagram) {
        // GIVEN - single class with expected method, with/without args/return type
        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                diagram, "ClassOne", "methodOne", "ReturnType", null);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "class ClassOne {\nmethodOne(ArgType varname)\n}",
            "class ClassOne {\nmethodOne(varname: ArgType)\n}",
            "class ClassOne {\nmethodOne(String one, ArgType varname, String two)\n}",
            "class ClassOne {\nmethodOne(String, ArgType, String)\n}",
            "class ClassOne {\nmethodOne(one : String, varname: ArgType, two: String)\n}",
            "class ClassOne {\nmethodOne(ArgType varname) : ReturnType\n}",
            "class ClassOne {\nmethodOne(varname: ArgType):ReturnType\n}",
            "class ClassOne {\nmethodOne(String one, ArgType varname, String two): ReturnType\n}",
            "class ClassOne {\nmethodOne(String, ArgType, String) : ReturnType\n}",
            "class ClassOne {\nmethodOne(one : String, varname: ArgType, two: String) : ReturnType\n}",
            "class ClassOne {\nanotherMethod(): void\nmethodOne(ArgType) : ReturnType\n}"
    })
    void
    assertClassDiagramTypeContainsMethod_methodNameAndArgType_noAssertFires(String diagram) {
        // GIVEN - single class with expected method, with/without args/return type
        // list of args to look for
        List<String> argTypes = ImmutableList.of("ArgType");
        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                diagram, "ClassOne", "methodOne", null, argTypes);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "class ClassOne {\nmethodOne(ArgType varname) : ReturnType\n}",
            "class ClassOne {\nmethodOne(varname: ArgType):ReturnType\n}",
            "class ClassOne {\nmethodOne(String one, ArgType varname, String two): ReturnType\n}",
            "class ClassOne {\nmethodOne(String, ArgType, String) : ReturnType\n}",
            "class ClassOne {\nmethodOne(one : String, varname: ArgType, two: String) : ReturnType\n}",
    })
    void
    assertClassDiagramTypeContainsMethod_methodNameReturnTypeAndArgType_noAssertFires(String diagram) {
        // GIVEN - single class with expected method, with/without args/return type
        // list of args to look for
        List<String> argTypes = ImmutableList.of("ArgType");
        // WHEN + THEN - no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                diagram, "ClassOne", "methodOne", "ReturnType", argTypes);
    }

    @Test
    void assertClassDiagramTypeContainsMethod_methodWithMultipleArgTypes_noAssertFires() {
        // GIVEN - class with method with multiple arg types
        String diagram = "class ClassOne{\nmethodOne(varname1: ArgType1, varname2: ArgType2) : ReturnType\n}";
        List<String> argTypes = ImmutableList.of("ArgType1", "ArgType2");

        // WHEN + THEN - all are found; no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                diagram, "ClassOne", "methodOne", "ReturnType", argTypes
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_typeMissing_assertFires() {
        // GIVEN - class has method but class has wrong name
        // missing class
        String missingClassName = "WrongClass";

        // WHEN + THEN - not found, assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                        CLASS_WITH_METHOD,
                        missingClassName,
                        "methodOne",
                        "ReturnType",
                        ImmutableList.of("ArgType"))
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_methodOnWrongClass_assertFires() {
        // GIVEN - a class has method but different from the one requested
        // class in diagram but without the expected method
        String missingClassName = "OtherClass";

        // WHEN + THEN - not found, assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                        CLASS_WITH_METHOD,
                        missingClassName,
                        "methodOne",
                        "ReturnType",
                        ImmutableList.of("ArgType"))
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_wrongMethodName_assertFires() {
        // GIVEN - class has a method with different name from requested
        // wrong method name
        String missingMethod = "wrongMethod";

        // WHEN + THEN - not found, assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                        CLASS_WITH_METHOD,
                        "ClassOne",
                        missingMethod,
                        "ReturnType",
                        ImmutableList.of("ArgType"))
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_missingReturnType_assertFires() {
        // GIVEN - diagram with method without return type
        // WHEN + THEN - not found, assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                        CLASS_WITH_METHOD_NO_RETURN,
                        "ClassOne",
                        "methodOne",
                        "ReturnType",
                        ImmutableList.of("ArgType"))
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_wrongReturnType_assertFires() {
        // GIVEN - class has a method with different return type from requested
        // wrong return type
        String wrongReturnType = "WrongReturnType";

        // WHEN + THEN - not found, assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                        CLASS_WITH_METHOD,
                        "ClassOne",
                        "methodOne",
                        wrongReturnType,
                        ImmutableList.of("ArgType"))
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_missingArgType_assertFires() {
        // GIVEN - diagram with method without args
        // WHEN + THEN - not found, assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                        CLASS_WITH_METHOD_NO_ARG,
                        "ClassOne",
                        "methodOne",
                        "ReturnType",
                        ImmutableList.of("ArgType"))
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_missingOneArgType_assertFires() {
        // GIVEN - diagram with method with two args
        String diagram = "class ClassOne{\nmethodOne(arg1: ArgType) : ReturnType\n}";
        // expected arg types
        List<String> argTypes = ImmutableList.of("ArgType", "String");

        // WHEN + THEN - second arg type not found, assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                        diagram, "ClassOne", "methodOne", "ReturnType", argTypes)
        );
    }

    // assertClassDiagramTypeContainsMethod(content, type, methodName)

    @Test
    void assertClassDiagramTypeContainsMethod_methodIsContained_noAssertFires() {
        // GIVEN -- single class with single method
        // WHEN + THEN - that method is found, no assert failure
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                CLASS_ONE, "ClassOne", "getMember1"
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_methodNotContained_assertFires() {
        // GIVEN -- single class with single method
        // WHEN + THEN - nonmethod is not found - assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                        CLASS_ONE, "ClassOne", "missingMethod")
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_methodExistsInDifferentClass_assertFires() {
        // GIVEN -- two classes
        // WHEN + THEN - don't find one class's method in the other - assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                        TWO_CLASSES, "ClassOne", "getMember2")
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_abstractMethodExistsInClass_noAssertFires() {
        // GIVEN -- class with abstract method
        // WHEN + THEN - method is found
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                CLASS_WITH_ABSTRACT_METHOD, "Abstracty", "implementMe"
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_staticMethodExistsInClass_noAssertFires() {
        // GIVEN -- class with abstract method
        // WHEN + THEN - method is found
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                CLASS_WITH_STATIC_METHOD, "Staticky", "classMethod"
        );
    }

    @Test
    void assertClassDiagramTypeContainsMethod_interfaceWithMethod_noAssertFires() {
        // GIVEN - interface with <<interface>> annotation
        // WHEN + THEN - method is found
        PlantUmlClassDiagramAssertions.assertClassDiagramTypeContainsMethod(
                INTERFACE_WITH_METHOD, "Quotable", "quote"
        );
    }
}
