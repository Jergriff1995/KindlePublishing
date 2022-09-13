package com.amazon.ata.test.helper;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.amazon.ata.test.helper.AtaTestHelper.matchesMultiLine;
import static com.amazon.ata.test.helper.AtaTestHelper.matchesSingleLine;

public class PlantUmlClassDiagramHelper {

    private PlantUmlClassDiagramHelper() {}


    /**
     * Find all types related to your given type in a class diagram.
     * @param content The contents of the Plant UML diagram
     * @param type The type you want to find relationships to
     * @return All types related to type
     */
    public static Set<String> getClassDiagramRelatedTypes(final String content, final String type) {
        Function<String, Set<String>> findRelated = patternToMatch -> {
            Set<String> desiredGroups = new HashSet<>();

            Pattern regexPattern = Pattern.compile(patternToMatch);
            Matcher matcher = regexPattern.matcher(content);

            while (matcher.find()) {
                desiredGroups.add(matcher.group(2));
            }

            return desiredGroups;
        };

        String javaClassNamePattern = "([a-zA-Z_$][a-zA-Z0-9_$]*)";
        // <type> --o  RelatedType
        String relatedTypeLeftPattern = "(%s.*[-.]+\\S*\\s*" + javaClassNamePattern + ")";
        // RelatedType ..> <type>
        // This pattern is very greedy and can take a long time to search due to "Catastrophic Backtracking."
        // If this becomes more of an issue, add ^/s to the beginning of the pattern to better anchor it.
        String relatedTypeRightPattern = "(" + javaClassNamePattern + ".*[-.]+.*%s)";

        return Stream.of(relatedTypeLeftPattern, relatedTypeRightPattern)
                .map(relationshipPattern -> String.format(relationshipPattern, type))
                .map(findRelated)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Determine if a Plant UML class diagram includes a relationship of any kind between the specified classes
     * (represented as {@code String}s).
     *
     * @param content The contents of the Plant UML diagram
     * @param typeOne One side of the relationship (represented as a {@code String}
     * @param typeTwo The other side of the relationship (represented as a {@code String}
     * @return Whether the class diagram includes the given relationship
     */
    public static boolean classDiagramIncludesRelationship(final String content,
                                                              final String typeOne,
                                                              final String typeTwo) {
        Set<String> relatedToTypeOne = getClassDiagramRelatedTypes(content, typeOne);
        return relatedToTypeOne.contains(typeTwo);
    }

    /**
     * Determine if a Plant UML class diagram includes a "contains" relationship between the specified classes
     * (represented as {@code String}s).
     *
     * {@code containingType} (the "from" type) is the type where the relationship originates from (it contains {@code
     * containedType}). {@code containedType} (the "to" type) is the type that is contained.
     *
     * @param content The contents of the Plant UML diagram
     * @param containingType The "from" side of the relationship (represented as a {@code String}
     * @param containedType The "to" side of the relationship (represented as a {@code String}
     * @return Whether the class diagram includes the given extends relationship
     */
    public static boolean classDiagramIncludesContainsRelationship(final String content,
                                                                  final String containingType,
                                                                  final String containedType) {
        return matchesSingleLine(
                content,
                patternForDiagramIncludesContainsRelationship(containingType, containedType));
    }

    /**
     * Determine if a Plant UML class diagram includes an "extends" relationship between the specified classes
     * (represented as {@code String}s).
     *
     * {@code subType} (the "from" type) is the type where the relationship originates from (it extends {@code
     * superType}). {@code superType} (the "to" type) is the type that is extended.
     *
     * @param content The contents of the Plant UML diagram
     * @param subType The "from" side of the relationship (represented as a {@code String}
     * @param superType The "to" side of the relationship (represented as a {@code String}
     * @return Whether the class diagram includes the given extends relationship
     */
    public static boolean classDiagramIncludesExtendsRelationship(final String content,
                                                                     final String subType,
                                                                     final String superType) {
        return matchesSingleLine(
                content,
                patternForDiagramIncludesExtendsRelationship(subType, superType));
    }

    /**
     * Determine if the class diagram includes the expected member in the specified type (class).
     *
     * @param content The contents of the Plant UML diagram
     * @param type The type/class being inspected
     * @param includedMember The member that we expect to be present in {@code type}. This should probably be
     *                       a variable name, but could also be a type (as long as no other members share this type)
     * @return Whether the type in the class diagram contained the expected member
     */
    public static boolean classDiagramTypeContainsMember(
            final String content,
            final String type,
            final String includedMember) {
        return matchesMultiLine(content, patternForTypeIncludesMember(type, includedMember));
    }

    private static String patternForDiagramIncludesAnyRelationship(final String typeOne, final String typeTwo) {
        // allow relationship of any kind in either direction
        List<String> validPatterns = ImmutableList.of(
                // typeOne -- typeTwo    typeOne .. typeTwo
                String.format("(%s.*[-.]+.*%s)", typeOne, typeTwo),
                // typeTwo -- typeOne    typeTwo .. typeOne
                String.format("(%s.*[-.]+.*%s)", typeTwo, typeOne)
        );

        return validPatterns.stream().collect(Collectors.joining("|"));
    }

    private static String patternForDiagramIncludesContainsRelationship(
            final String containingType, final String containedType) {
        // allow the relationship to be expressed in either direction, with the aggregation/composition diamond
        // always on the containingType side. Allow for arrowheads on the "to" side of relationship
        List<String> validPatterns = ImmutableList.of(
                // containingType *--> containedType containingType o--> containedType containingType --> containedType
                String.format("(%s.* [o*]?-+> .*%s)", containingType, containedType),
                // containedType <--* containingType containedType <--o containingType containedType <--, containingType
                String.format("(%s.* <-+[o*]? .*%s)", containedType, containingType),
                // containingType *-- containedType  containingType o-- containedType  containingType -- containedType
                String.format("(%s.* [o*]?-+ .*%s)", containingType, containedType),
                // containedType --* containingType   containedType --o containingType   containedType -- containingType
                String.format("(%s.* -+[o*]? .*%s)", containedType, containingType));

        return validPatterns.stream().collect(Collectors.joining("|"));
    }

    private static String patternForDiagramIncludesExtendsRelationship(final String subType, final String superType) {
        // allow the extends relationship to be in either direction, but require the closed arrow shape: |>
        List<String> validPatterns = ImmutableList.of(
                // subType --|> superType
                String.format("(%s.*-+\\|>.*%s)", subType, superType),
                // superType <|-- subType
                String.format("(%s.*<\\|-+.*%s)", superType, subType)
        );

        return validPatterns.stream().collect(Collectors.joining("|"));
    }

    private static String patternForTypeIncludesMember(final String type, final String member) {
        // class/enum/interface TypeOne {
        //     ...
        //     -expectedMember
        return String.format("%s\\s*\\{[^}]*%s", type, member);
    }

}
