package com.amazon.ata.test.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlantUmlClassDiagramHelperTest {
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

    // getClassDiagramRelatedTypes
    @ParameterizedTest
    @ValueSource(strings = {RIGHT_SIMPLE, LEFT_SIMPLE, RIGHT_AGGREGATION, LEFT_AGGREGATION, RIGHT_COMPOSITION,
            LEFT_COMPOSITION, RIGHT_IMPLEMENTS, LEFT_IMPLEMENTS, RIGHT_EXTENDS, LEFT_EXTENDS})
    public void getClassDiagramRelatedTypes_relationshipBetweenClassOneAndTwo_returnsClassOne(String diagramIncludingRelationship) {
        // GIVEN -- relationship to ClassTwo
        // WHEN
        Set<String> relatedTypes = PlantUmlClassDiagramHelper.getClassDiagramRelatedTypes(
                diagramIncludingRelationship, "ClassTwo"
        );

        // THEN
        assertEquals(1, relatedTypes.size());
        assertTrue(relatedTypes.contains("ClassOne"));
    }

    @Test
    public void getClassDiagramRelatedType_noRelatedTypes_returnsEmptyList() {
        // GIVEN
        String noRelatedTypes = "class One {} \nClassTwo {}";

        // WHEN
        Set<String> relatedTypes = PlantUmlClassDiagramHelper.getClassDiagramRelatedTypes(
                noRelatedTypes, "ClassTwo"
        );

        // THEN
        assertTrue(relatedTypes.isEmpty());
    }

    @Test
    public void getClassDiagramRelatedType_contentHasLotsOfNonWhitespace_executesInLessThanX() {
        // GIVEN - content with lots of on non-whitespace (this URL caused timeouts previously)
        String content = "https://plantuml.corp.amazon.com/plantuml/form/encoded.html#encoded=dLRRYjim47ttL-WnfVW7oh8" +
                "atVGIkYrbTzlNCB46HwmikTOur9JzzqhnJV9j5qCWiUQSEQEZOv6j8IYep6HadeNAeO2CIQ6GgXnPhcNb7gPgkg2pZg8SacT8aK5" +
                "2uYAeOl-YA95W3Djn_gXL-gH_Ih1T3Erw26Enr2dxmEvrcc9Xd-td_HaKb_Y0Vqeqj1dncy8xIvkecL8IYrvQzRi2WN2bT4ZsT8S" +
                "59zfhVa5QAUWnFRLWTLRA6M7Mwrtfqw9DALJ-bVFrnePaJtLOQ3HV-e3nIa6c-Sa5h5sppoYD5pbvAhEdmbQlysptLyx-urSmvsz" +
                "OCJ12MR57AeHAVS03g1GTWXpvgChiX8L57XHXlPSONE-ZXQFV8JRMeoC9ajvovrMxd-bf10ywZLedfI6TON7W8yKxNIeAvoD8ZSC" +
                "XtTJOsnPyxoQmnsA_Rdg7RcjxXyRjGTTdoPjflqEUNxtO1mlZJRwPMGSZxNEsgGUsXhWejAiEPE3IXMBJkPatFnpweWKFWu9_4yn" +
                "9Q3KPZ1-QFblp-ASMPK7TLWxg0bBm7TbvJYNXRv0bJYBRMQRRMTZyxQVZsFVi5MHmCj7ChJPiENKHBM326sG8d7dXXx3kTCAf8py" +
                "U7YMQT_3D_dZ0KPTbsa8UverPb9jnQILdEOYJvxkeD9ERrvN2Yo4ysIqgRlyu_0S0";

        // WHEN
        Instant start = Instant.now();
        Set<String> relatedTypes = PlantUmlClassDiagramHelper.getClassDiagramRelatedTypes(content, "ClassTwo");
        Instant stop = Instant.now();

        assertTrue(relatedTypes.isEmpty(), "There shouldn't be any matching types in the content.");
        long latency = Duration.between(start, stop).toMillis();
        assertTrue(latency < 2_000,
                String.format("Searching content should finish in less than 2 seconds. Took %s ms.", latency));
    }

    // classDiagramIncludesRelationship

    @ParameterizedTest
    @ValueSource(strings = {
            RIGHT_SIMPLE, LEFT_SIMPLE,
            RIGHT_COMPOSITION, LEFT_COMPOSITION,
            RIGHT_AGGREGATION, LEFT_AGGREGATION,
            RIGHT_IMPLEMENTS, LEFT_IMPLEMENTS,
            RIGHT_EXTENDS, LEFT_EXTENDS})
    void classDiagramIncludesRelationship_relationshipExists_returnsTrue(String diagramIncludingRelationship) {
        // GIVEN -- diagram with relationship from ClassOne to ClassTwo
        // WHEN + THEN -- returns true
        assertTrue(PlantUmlClassDiagramHelper.classDiagramIncludesRelationship(
                diagramIncludingRelationship, "ClassOne", "ClassTwo"
        ));
    }

    @Test
    void classDiagramIncludesRelationship_commaDelimitedListOfClasses_returnsFalse() {
        // GIVEN -- diagram with classes in a comma-delimited list
        String diagram = "ClassOne, ClassTwo";

        // WHEN + THEN -- returns false
        assertFalse(PlantUmlClassDiagramHelper.classDiagramIncludesRelationship(
                        diagram, "ClassOne", "ClassTwo")
        );
    }

    @Test
    void classDiagramIncludesRelationship_oneClassButNotTheOther_returnsFalse() {
        // GIVEN -- diagram with relationship including one class but not the other
        // WHEN + THEN -- returns false
        assertFalse(PlantUmlClassDiagramHelper.classDiagramIncludesRelationship(
                        RIGHT_COMPOSITION, "ClassOne", "ClassSix")
        );
    }

    @Test
    void classDiagramIncludesRelationship_classesHaveRelationshipsWithOtherClasses_returnsFalse() {
        // GIVEN -- two relationships among three classes, one pairing is excluded
        String associations = "ClassOne -- ClassTwo\nClassTwo -- ClassThree";

        // WHEN + THEN
        assertFalse(PlantUmlClassDiagramHelper.classDiagramIncludesRelationship(
                        associations, "ClassOne", "ClassThree")
        );
    }


    // classDiagramIncludesExtendsRelationship(String, String)

    @ParameterizedTest
    @ValueSource(strings = {RIGHT_EXTENDS, LEFT_EXTENDS})
    void classDiagramIncludesExtendsRelationship_String_extends_returnsTrue(String diagram) {
        // GIVEN -- diagram with extends relationship that matches
        // WHEN + THEN -- returns true
        assertTrue(PlantUmlClassDiagramHelper.classDiagramIncludesExtendsRelationship(
                diagram,"ClassOne", "ClassTwo"
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            RIGHT_COMPOSITION, LEFT_COMPOSITION,
            RIGHT_AGGREGATION, LEFT_AGGREGATION,
            RIGHT_IMPLEMENTS, LEFT_IMPLEMENTS})
    void classDiagramIncludesExtendsRelationship_String_wrongRelationshipType_returnsFalse(String diagram) {
        // GIVEN -- diagram with relationship in correct direction between correct types, but write relationship type
        // WHEN + THEN -- returns false
        assertFalse(PlantUmlClassDiagramHelper.classDiagramIncludesExtendsRelationship(
                        diagram, "ClassOne", "ClassTwo")
        );
    }

    @Test
    void classDiagramIncludesExtendsRelationship_String_extendsReversed_returnsFalse() {
        // GIVEN -- extends relationship, but in wrong direction
        // WHEN + THEN -- returns false
        assertFalse(PlantUmlClassDiagramHelper.classDiagramIncludesExtendsRelationship(
                RIGHT_EXTENDS, "ClassTwo", "ClassOne")
        );
    }

    // classDiagramTypeContainsMember

    @Test
    void classDiagramTypeContainsMember_memberIsContained_returnsTrue() {
        // GIVEN -- single class with single member
        // WHEN + THEN - that member is found, returns true
        assertTrue(PlantUmlClassDiagramHelper.classDiagramTypeContainsMember(
                CLASS_ONE, "ClassOne", "member1"));
    }

    @Test
    void classDiagramTypeContainsMember_memberNotContained_returnsFalse() {
        // GIVEN -- single class with single member
        // WHEN + THEN - nonmember is not found - returns false
        assertFalse(PlantUmlClassDiagramHelper.classDiagramTypeContainsMember(
                        CLASS_ONE, "ClassOne", "notInHere")
        );
    }

    @Test
    void classDiagramTypeContainsMember_memberExistsInDifferentClass_returnsFalse() {
        // GIVEN -- two classes
        // WHEN + THEN - don't find one class's member in the other - returns false
        assertFalse(PlantUmlClassDiagramHelper.classDiagramTypeContainsMember(
                        TWO_CLASSES, "ClassOne", "member2")
        );
    }
}
