package com.amazon.ata.test.assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlantUmlSequenceDiagramAssertionsTest {
    // assertSequenceDiagramContains

    @Test
    void assertSequenceDiagramContains_stringMatches_noAssertFires() {
        // GIVEN -- matching string
        String matchingClass = "Automobile";

        // WHEN + THEN -- no assert failure
        PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContains("Automobile --> Engine", matchingClass);
    }

    @Test
    void assertSequenceDiagramContains_stringDoesntMatch_assertFires() {
        // GIVEN -- mon-matching string
        String nonMatchingClass = "Taxi";

        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContains(
                        "Automobile --> Engine", nonMatchingClass)
            );
    }

    // assertSequenceDiagramContainsEntity

    // PlantUmlSequenceDiagramHelperTest has more comprehensive set of cases
    @ParameterizedTest
    @ValueSource(strings = {
        "Automobile -> Engine: method()",
        "Automobile -->   Engine",
        "Engine<- Automobile:method()",
        "Engine<--Automobile:ReturnType",
        "participant    Automobile as Auto"
    })
    void assertSequenceDiagramContainsEntity_stringMatches_noAssertFires(String content) {
        // GIVEN -- matching string
        String matchingClass = "Automobile";

        // WHEN + THEN -- no assert failure
        PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsEntity(content, matchingClass);
    }

    @Test
    void assertSequenceDiagramContainsEntity_stringNowhereInDiagram_assertFires() {
        // GIVEN -- mon-matching string
        String nonMatchingClass = "Taxi";

        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsEntity(
                        "Automobile --> Engine", nonMatchingClass)
        );
    }

    // PlantUmlSequenceDiagramHelperTest has more comprehensive set of cases
    @ParameterizedTest
    @ValueSource(strings = {
        "SlowAutomobile -> Engine : method()",
        "AutomobileType<--Engine : ReturnType",
        "SlowAutomobileType<--Engine",
        "participant AutomobileType",
        "participant SlowAutomobile",
        "participant SlowAutomobileType"
    })
    void assertSequenceDiagramContainsEntity_entityNameIsSubstringOfEntityInDiagram_assertFires(String content) {
        // GIVEN -- mon-matching string
        String nonMatchingClass = "Automobile";

        // WHEN + THEN -- assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsEntity(
                content, nonMatchingClass)
        );
    }

    // PlantUmlSequenceDiagramHelperTest has more comprehensive set of cases
    @ParameterizedTest
    @ValueSource(strings = {
        "Automobile --> Engine: ReturnType",
        "Automobile -> Engine: getReturnType",
        "' Comment with ReturnType"
    })
    void assertSequenceDiagramContainsEntity_stringExistsButIsntAnEntity_assertFires(String content) {
        // GIVEN - string that exists in the diagram content, but not as an entity
        String nonMatchingClass = "ReturnType";

        // WHEN + THEN -- assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsEntity(
                content, nonMatchingClass)
        );
    }

    // assertSequenceDiagramContainsReturnType

    @Test
    void assertSequenceDiagramContainsReturnType_stringMatches_noAssertFires() {
        // GIVEN -- matching string
        String matchingType = "StartRequest";

        // WHEN + THEN -- no assert failure
        PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsReturnType(
                "Automobile --> Engine: StartRequest", matchingType);
    }

    @Test
    void assertSequenceDiagramContainsReturnType_stringDoesntMatch_assertFires() {
        // GIVEN -- mon-matching string
        String nonMatchingType = "StopRequest";

        // GIVEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsReturnType(
                        "Automobile --> Engine: StartRequest", nonMatchingType)
        );
    }

    // assertSequenceDiagramContainsMethod

    @Test
    void assertSequenceDiagramContainsMethod_stringMatches_noAssertFires() {
        // GIVEN -- matching string
        String matchingMethod = "start()";

        // WHEN + THEN -- no assert failure
        PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsMethod(
                "Automobile --> Engine: start()", matchingMethod);
    }

    @Test
    void assertSequenceDiagramContainsMethod_stringDoesntMatch_assertFires() {
        // GIVEN -- mon-matching string
        String nonMatchingMethod = "stop()";

        // WHEN + THEN -- assert failure
        assertThrows(
                AssertionFailedError.class,
                () -> PlantUmlSequenceDiagramAssertions.assertSequenceDiagramContainsMethod(
                        "Automobile --> Engine: start()", nonMatchingMethod)
        );
    }
}
