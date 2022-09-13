package com.amazon.ata.kindlepublishingservice.mastery.mt1.design;

import com.amazon.ata.test.assertions.PlantUmlClassDiagramAssertions;
import com.amazon.ata.test.helper.AtaTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.amazon.ata.test.assertions.PlantUmlClassDiagramAssertions.*;
import static com.amazon.ata.test.helper.PlantUmlClassDiagramHelper.classDiagramTypeContainsMember;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("MT1-Design")
public class MasteryTaskOneDesignTests {
    private static final String CLASS_DIAGRAM_PATH = "mastery-task1-kindle-publishing-CD.puml";

    private String content;

    @BeforeEach
    public void setup() {
        content = AtaTestHelper.getFileContentFromResources(CLASS_DIAGRAM_PATH);
    }

    @Test
    void mt1Design_getClassDiagram_nonEmptyFileExists() {
        assertFalse(content.trim().isEmpty(),
                String.format("Expected file: %s to contain class diagram but was empty", CLASS_DIAGRAM_PATH));
    }

    @ParameterizedTest
    @ValueSource(strings = {"GetBookActivity", "RemoveBookFromCatalogActivity", "SubmitBookForPublishingActivity",
            "RecommendationsServiceClient", "CatalogItemVersion", "PublishingStatusItem",
            "CatalogDao", "PublishingStatusDao", "BookNotFoundException"})
    void mt1Design_getClassDiagram_containsClasses(String packagingClass) {
        PlantUmlClassDiagramAssertions.assertClassDiagramContainsClass(content, packagingClass);
    }

    @ParameterizedTest
    @MethodSource("containsRelationshipProvider")
    void mt1Design_getClassDiagram_includesExpectedContainsRelationships(String containingType, String containedType) {
        assertClassDiagramIncludesContainsRelationship(content, containingType, containedType);
    }

    private static Stream<Arguments> containsRelationshipProvider() {
        return Stream.of(
                //            containingType     containedType
                Arguments.of("GetBookActivity", "CatalogDao"),
                Arguments.of("GetBookActivity", "RecommendationsServiceClient"),
                Arguments.of("SubmitBookForPublishingActivity", "PublishingStatusDao")
        );
    }

    @ParameterizedTest
    @MethodSource("anyRelationshipProvider")
    void mt1Design_getClassDiagram_includesExpectedRelationships(String firstType, String secondType) {
        assertClassDiagramIncludesRelationship(content, firstType, secondType);
    }

    private static Stream<Arguments> anyRelationshipProvider() {
        return Stream.of(
                Arguments.of("CatalogItemVersion", "CatalogDao"),
                Arguments.of("PublishingStatusItem", "PublishingStatusDao"),
                Arguments.of("CatalogDao", "BookNotFoundException")
        );
    }

    @Test
    void mt1Design_getClassDiagram_containsCatalogItemVersionFields() {
        assertClassDiagramTypeContainsMember(
                content, "CatalogItemVersion", "@DynamoDBHashKey\\s*bookId\\s*:\\s*String");
        assertClassDiagramTypeContainsMember(
                content, "CatalogItemVersion", "@DynamoDBRangeKey\\s*version\\s*:\\s*int");
        assertClassDiagramTypeContainsMember(
                content, "CatalogItemVersion", "inactive\\s*:\\s*boolean");
        assertClassDiagramTypeContainsMember(
                content, "CatalogItemVersion", "author\\s*:\\s*String");
        assertClassDiagramTypeContainsMember(
                content, "CatalogItemVersion", "text\\s*:\\s*String");
        assertClassDiagramTypeContainsMember(
                content, "CatalogItemVersion", "genre\\s*:\\s*BookGenre");
    }

    @Test
    void mt1Design_getClassDiagram_containsPublishingStatusItemFields() {
        assertClassDiagramTypeContainsMember(
                content, "PublishingStatusItem", "@DynamoDBHashKey\\s*publishingRecordId\\s*:\\s*String");
        assertClassDiagramTypeContainsMember(
                content, "PublishingStatusItem", "@DynamoDBRangeKey\\s*status\\s*:\\s*PublishingRecordStatus");
        assertClassDiagramTypeContainsMember(
                content, "PublishingStatusItem", "statusMessage\\s*:\\s*String");
        assertClassDiagramTypeContainsMember(
                content, "PublishingStatusItem", "bookId\\s*:\\s*String");
    }

    @ParameterizedTest
    @ValueSource(strings = {"CatalogDao", "PublishingStatusDao"})
    void mt1Design_getClassDiagram_daosContainDynamoDBMapper(String type) {
        // Testing different capitalization of the DynamoDBMapper
        boolean containsDynamoDBMapper = classDiagramTypeContainsMember(
                content, type,  "dynamoDBMapper\\s*:\\sDynamoDBMapper");
        boolean containsDynamoDbMapper = classDiagramTypeContainsMember(
                content, type,  "dynamoDbMapper\\s*:\\sDynamoDBMapper");
        assertTrue(containsDynamoDBMapper || containsDynamoDbMapper,
                String.format("In class diagram, %s might be missing the DynamoDBMapper member variable, " +
                        "or it may be formatted incorrectly, expecting `variableName : type`.", type));
    }

    @ParameterizedTest
    @ValueSource(strings = {"GetBook", "RemoveBookFromCatalog", "SubmitBookForPublishing"})
    void mt1Design_getClassDiagram_activitiesContainMethods(String name) {
        String type = name + "Activity";
        String returnType = name + "Response";
        List<String> arguments = Arrays.asList(name + "Request");
        assertClassDiagramTypeContainsMethod(content, type, "execute", returnType, arguments);
    }
}
