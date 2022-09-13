package com.amazon.ata.test.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlantUmlSequenceDiagramHelperTest {

    // sequenceDiagramContains

    @Test
    void sequenceDiagramContains_stringMatches_returnsTrue() {
        // GIVEN -- matching string
        String matchingClass = "Automobile";

        // WHEN + THEN -- returns true
        assertTrue(PlantUmlSequenceDiagramHelper.sequenceDiagramContains(
                "Automobile --> Engine", matchingClass));
    }

    @Test
    void sequenceDiagramContains_stringDoesntMatch_returnsFalse() {
        // GIVEN -- mon-matching string
        String nonMatchingClass = "Taxi";

        // GIVEN + THEN -- returns false
        assertFalse(PlantUmlSequenceDiagramHelper.sequenceDiagramContains(
                "Automobile --> Engine", nonMatchingClass));
    }

    // sequenceDiagramContainsEntity

    @ParameterizedTest
    @ValueSource(strings = {
        "Automobile -> Engine: method()",
        "Automobile -->   Engine",
        "Automobile<-Engine : method()",
        "Automobile<--Engine : ReturnType",
        "Engine ->Automobile    : method()",
        "Engine -->Automobile   : ReturnType",
        "Engine<- Automobile:method()",
        "Engine<--Automobile:ReturnType",
        "Automobile -> Automobile",
        "Engine -> Axle\nAutomobile -> Engine",
        "Engine -> Axle\nEngine -> Automobile",
        "participant    Automobile as Auto",
        "'Comment\nAutomobile -> Engine",
        "'Comment\nEngine<--Automobile",
        "'Comment\nparticipant Automobile",
        " ' Comment\nAutomobile -> Engine",
        " ' Comment\nEngine<--Automobile",
        " ' Comment\nparticipant Automobile",
        "Something -> OrOther\n'Comment\nAutomobile -> Engine",
        "Something -> OrOther\n'Comment\nEngine<--Automobile",
        "Something -> OrOther\n'Comment\nparticipant Automobile"
    })
    void sequenceDiagramContainsEntity_entityExists_returnsTrue(String content) {
        // GIVEN -- matching string
        String entity = "Automobile";

        // WHEN
        boolean result = PlantUmlSequenceDiagramHelper.sequenceDiagramContainsEntity(content, entity);

        // THEN
        assertTrue(result, String.format("Expected to find entity, '%s', in '%s'", entity, content));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Automobile --> Engine: ReturnType",
        "Automobile --> Engine: ReturnType",
        "Automobile -> Engine: getReturnType",
        "Automobile -> Engine: getReturnType()"
    })
    void sequenceDiagramContainsEntity_stringExistsButIsntAnEntity_returnsFalse(String content) {
        // GIVEN - string that exists in the diagram content, but not as an entity
        String entity = "ReturnType";

        // WHEN
        boolean result = PlantUmlSequenceDiagramHelper.sequenceDiagramContainsEntity(content, entity);

        // THEN
        assertFalse(result, String.format("Expected not to find entity, '%s', in diagram, '%s', but did.",
                                          entity,
                                          content));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "' Comment about Automobile",
        "'participant Automobile",
        "'Automobile -> Engine"
    })
    void sequenceDiagramContainsEntity_stringExistsButInComment_returnsFalse(String content) {
        // GIVEN - string that exists in the diagram content, but inside a comment
        String entity = "Automobile";

        // WHEN
        boolean result = PlantUmlSequenceDiagramHelper.sequenceDiagramContainsEntity(content, entity);

        // THEN
        assertFalse(result, String.format("Expected not to find entity, '%s', in diagram, '%s', but did.",
                                          entity,
                                          content));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "SlowAutomobile -> Engine : method()",
        "AutomobileType<--Engine : ReturnType",
        "SlowAutomobileType<--Engine",
        "Engine -> SlowAutomobile : method()",
        "Engine <-- AutomobileType : ReturnType",
        "Engine<--SlowAutomobileType",
        "participant AutomobileType",
        "participant SlowAutomobile as Auto",
        "participant SlowAutomobileType as Auto"
    })
    void sequenceDiagramContainsEntity_entityNameIsSubstringOfOtherEntity_returnsFalse(String content) {
        // GIVEN -- mon-matching string
        String entity = "Automobile";

        // WHEN
        boolean result = PlantUmlSequenceDiagramHelper.sequenceDiagramContainsEntity(content, entity);

        // THEN
        assertFalse(result, String.format("Expected not to find entity, '%s', in diagram, '%s', but did.",
                                          entity,
                                          content));
    }
}
