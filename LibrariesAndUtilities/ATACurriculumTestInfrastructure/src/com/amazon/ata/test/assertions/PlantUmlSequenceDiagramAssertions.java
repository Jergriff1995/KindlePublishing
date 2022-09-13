package com.amazon.ata.test.assertions;

import com.amazon.ata.test.helper.PlantUmlSequenceDiagramHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public final class PlantUmlSequenceDiagramAssertions {

    private PlantUmlSequenceDiagramAssertions() {
    }

    /**
     * Asserts that a Plant UML sequence diagram contains the given substring.
     *
     * @param content The contents of the Plant UML diagram
     * @param expectedString The substring to search for
     */
    public static void assertSequenceDiagramContains(final String content, final String expectedString) {
        assertTrue(PlantUmlSequenceDiagramHelper.sequenceDiagramContains(content, expectedString),
            String.format("Expected sequence diagram to include %s", expectedString)
        );
    }

    /**
     * Asserts that a Plant UML sequence diagram contains the given participant/actor/entity.
     *
     * @param content The contents of the Plant UML diagram
     * @param entity The substring to search for
     */
    public static void assertSequenceDiagramContainsEntity(final String content, final String entity) {
        boolean containsEntity = PlantUmlSequenceDiagramHelper.sequenceDiagramContainsEntity(content, entity);

        if (!containsEntity && PlantUmlSequenceDiagramHelper.sequenceDiagramContains(content, entity)) {
            fail(String.format("Sequence diagram appears to contain '%s', but it does not appear to be as an entity. " +
                               "%s should make/receive method calls, with -> or --> syntax.",
                               entity,
                               entity)
            );
        }
        assertTrue(containsEntity,
                   String.format("Expected sequence diagram to include a participant/actor/entity with name '%s'.",
                                 entity)
        );
    }

    /**
     * Asserts that a Plant UML sequence diagram contains the given substring.
     *
     * @param content The contents of the Plant UML diagram
     * @param returnType The substring to search for
     */
    public static void assertSequenceDiagramContainsReturnType(final String content, final String returnType) {
        boolean containsReturnType = PlantUmlSequenceDiagramHelper.sequenceDiagramContainsReturnType(content,
                                                                                                     returnType);

        if (!containsReturnType && PlantUmlSequenceDiagramHelper.sequenceDiagramContains(content, returnType)) {
            fail(String.format("Sequence diagram appears to contain '%s', but it does not appear to be as a return " +
                               "type. %s should be returned in a message, such as Driver <-- Car : %s",
                               returnType,
                               returnType,
                               returnType)
            );
        }

        assertTrue(containsReturnType,
                   String.format("Expected sequence diagram to include a return of type %s.", returnType)
        );
    }

    /**
     * Asserts that a Plant UML sequence diagram contains the given substring.
     *
     * @param content The contents of the Plant UML diagram
     * @param methodName The substring to search for
     */
    public static void assertSequenceDiagramContainsMethod(final String content, final String methodName) {
        boolean containsMethod = PlantUmlSequenceDiagramHelper.sequenceDiagramContainsMethod(content, methodName);

        if (!containsMethod && PlantUmlSequenceDiagramHelper.sequenceDiagramContains(content, methodName)) {
            fail(String.format("Sequence diagram appears to contain '%s', but it does not appear to be as a method. " +
                               "%s should be the method call in a message, such as Driver -> Car : %s()",
                               methodName,
                               methodName,
                               methodName)
            );
        }

        assertTrue(containsMethod,
                   String.format("Expected sequence diagram to include method with name %s.", methodName)
        );
    }
}
