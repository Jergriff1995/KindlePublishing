package com.amazon.ata.kindlepublishingservice.mastery.mt1.design;

import com.amazon.ata.test.assertions.PlantUmlSequenceDiagramAssertions;
import com.amazon.ata.test.helper.AtaTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("MT1-Design")
public class MasteryTaskOneSequenceTests {
    private static final String REMOVE_BOOK_SEQUENCE_DIAGRAM_PATH = "mastery-task1-remove-book-SD.puml";

    private String content;

    @BeforeEach
    public void setup() {
        content = AtaTestHelper.getFileContentFromResources(REMOVE_BOOK_SEQUENCE_DIAGRAM_PATH);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RemoveBookFromCatalogActivity", "CatalogDao"})
    void mt1Design_removeBookSequenceDiagram_includesExpectedTypes(String type) {
        PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsEntity(content, type);
    }

    @ParameterizedTest
    @ValueSource(strings = {"BookNotFoundException", "RemoveBookFromCatalogResponse"})
    void mt1Design_removeBookSequenceDiagram_includesExpectedReturnTypes(String type) {
        PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsReturnType(content, type);
    }

    @ParameterizedTest
    @ValueSource(strings = {"execute", "removeBookFromCatalog"})
    void mt1Design_removeBookSequenceDiagram_includesExpectedMethodCalls(String method) {
        PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsMethod(content, method);
    }
}
