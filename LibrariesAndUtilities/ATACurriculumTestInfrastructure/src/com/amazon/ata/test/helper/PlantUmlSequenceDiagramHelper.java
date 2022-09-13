package com.amazon.ata.test.helper;

import static com.amazon.ata.test.helper.FreeFormTextHelper.matchesMultiLine;

/**
 * Utility methods to determine if a sequence diagram contains specific content.
 * Decent PlantUML reference visible on Amazon network:
 * http://wiki.plantuml.net/start
 */
public class PlantUmlSequenceDiagramHelper {

    private PlantUmlSequenceDiagramHelper() {}

    /**
     * Determines if a Plant UML sequence diagram contains the given substring.
     *
     * @param content The contents of the Plant UML diagram
     * @param expectedString The substring to search for
     * @return Whether the expectedString was found in the content
     */
    public static boolean sequenceDiagramContains(final String content, final String expectedString) {
        return content.contains(expectedString);
    }

    /**
     * Determines if a Plant UML sequence diagram contains the given entity/participant.
     *
     * The given string must play the role of a participant in the sequence diagram,
     * either explicitly with 'participant/actor/database Entity' or sending/receiving messages
     * from another entity (e.g. {@code Entity -> OtherEntity}, {@code OtherEntity --> Entity}).
     *
     * @param content The contents of the Plant UML diagram
     * @param entity The entity name to search for
     * @return true if the entity was found in the content; false otherwise
     */
    public static boolean sequenceDiagramContainsEntity(final String content, final String entity) {
        // WARNING: do NOT use dot ('.') in any of these patterns, as it'll span lines!
        // (using multiline matching so we can use ^ to match beginning of any line, but
        // our match method also applies DOTALL in that case)

        String boundedEntity = String.format("\\b%s\\b", entity);

        // e.g. participant Entity, actor    Entity
        String participantDeclarationPattern = String.format("^[^']*(participant|actor|database)\\s+%s", boundedEntity);
        // e.g. Entity -> Other, Entity-->Other, Entity <- Other
        String entityOnLeftSidePattern = String.format("^[^']*%s\\s*(-+>|<-+)", boundedEntity);
        // e.g. Other -> Entity, Other-->Entity, Other <- Entity
        String entityOnRightSidePattern = String.format("^[^']*(-+>|<-+)\\s*%s", boundedEntity);

        return matchesMultiLine(content, participantDeclarationPattern) ||
               matchesMultiLine(content, entityOnLeftSidePattern) ||
               matchesMultiLine(content, entityOnRightSidePattern);
    }

    /**
     * Determines if a Plant UML sequence diagram contains a message with the specified
     * return type.
     * TODO: Validate this is a return and not just present. Current version is more permissive
     *       than it needs to be.
     *
     * Must use the colon notation to indicate return type (e.g. {@code Entity --> Other : ReturnType}).
     * Note that it's forgiving on arrow type and will allow matching on
     * {@code Entity -> Other : ReturnType} as well.
     *
     * @param content The contents of the Plant UML diagram
     * @param returnType The return type to search for
     * @return true if the return type was found on a message; false otherwise
     */
    public static boolean sequenceDiagramContainsReturnType(final String content, final String returnType) {
        // TODO-ATAENG-3468: when it comes time to implement:
        // Something like ^[^']*(<-|->)[^:]*:\s*\\b[returntype]\\b
        // If want to force dotted line arrows, change arrows to <--|-->
        return content.contains(returnType);
    }

    /**
     * Determines if a Plant UML sequence diagram contains a message with the specified
     * method name attached to it.
     * TODO: Validate this is a return and not just present. Current version is more permissive
     *       than it needs to be.
     *
     * Must use the colon notation to indicate return type (e.g. {@code Entity -> Other : ReturnType})
     * @param content The contents of the Plant UML diagram
     * @param methodName The method name to search for
     * @return true if the method name was found on a message; false otherwise
     */
    public static boolean sequenceDiagramContainsMethod(final String content, final String methodName) {
        // TODO-ATAENG-3468: want this to match ^[^']*(<-|->)[^:]*:\s*\\b[methodname]\\b
        //  - not in a comment
        // probably same rule as contains return type...maybe share a method in the helper

        return content.contains(methodName);
    }
}
