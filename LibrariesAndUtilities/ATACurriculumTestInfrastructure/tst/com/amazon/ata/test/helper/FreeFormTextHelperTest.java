package com.amazon.ata.test.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static com.amazon.ata.test.helper.FreeFormTextHelper.getValueAfterPattern;
import static com.amazon.ata.test.helper.FreeFormTextHelper.getValueBetweenPatterns;
import static com.amazon.ata.test.helper.FreeFormTextHelper.matchesMultiLine;
import static com.amazon.ata.test.helper.FreeFormTextHelper.matchesSingleLine;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FreeFormTextHelperTest {
    private static final String QUESTION_1 = "What is your name?";
    private static final String ANSWER_1 = "It is 'Arthur', King of the Britons.";
    private static final String QUESTION_2 = "What is your quest?";
    private static final String ANSWER_2 = "To seek the Holy Grail.";
    private static final String QUESTION_3 = "What is the air-speed velocity of an unladen swallow?";
    private static final String ANSWER_3 = "What do you mean? \nAn African or European swallow?";

    private static final String MULTI_LINE = String.format("%s\n%s\n%s\n%s\n%s\n%s\n",
        QUESTION_1, ANSWER_1, QUESTION_2, ANSWER_2, QUESTION_3, ANSWER_3);
    private static final String SINGLE_LINE = String.format("%s%s%s%s%s%s",
        QUESTION_1, ANSWER_1, QUESTION_2, ANSWER_2, QUESTION_3, ANSWER_3);

    // matchesSingleLine

    @ParameterizedTest
    @ValueSource(strings = {"cd", "c.e"})
    void matchesSingleLine_patternMatches_returnsTrue(String matchingPattern) {
        // GIVEN -- matching pattern:
        //   - simple string pattern
        //   - wildcard that matches

        // WHEN + THEN -- returns true
        assertTrue(matchesSingleLine("ab\ncde\nfg", matchingPattern));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd", "ac+d", "a.*d"})
    void matchesSingleLine_patternDoesntMatch_returnsFalse(String nonMatchingPattern) {
        // GIVEN -- non-matching pattern:
        //   - simple string not in content
        //   - wildcard that doesn't match
        //   - wildcard that *spans* lines

        // WHEN + THEN -- returns false
        assertFalse(matchesSingleLine("ab\ncde\nfg", nonMatchingPattern));
    }

    // matchesMultiLine

    @ParameterizedTest
    @ValueSource(strings = {"cd", "c.e", "a.*d", "^c.e*"})
    void matchesMultiLine_patternMatches_returnsTrue(String matchingPattern) {
        // GIVEN -- matching pattern
        //   - simple string single line
        //   - wildcard on single line
        //   - wildcard across lines
        //   - matching beginning/end of internal line

        // WHEN + THEN -- returns true
        assertTrue(matchesMultiLine("ab\ncde\nfg", matchingPattern));
    }

    @ParameterizedTest
    @ValueSource(strings = {"gh", "ac+d"})
    void matchesMultiLine_patternDoesntMatch_returnsFalse(String nonMatchingPattern) {
        // GIVEN -- non-matching pattern
        //   - simple string single line
        //   - wildcard

        // WHEN + THEN -- returns false
        assertFalse(matchesMultiLine("ab\ncde\nfg", nonMatchingPattern));
    }

    @Test
    public void getValueBetweenPatterns_multiLine_returnsValue() {
        // GIVEN a multi line content
        // WHEN we search between two existing patterns
        Optional<String> match = getValueBetweenPatterns(
            MULTI_LINE,
            "What is your name\\?",
            "What is your quest\\?");

        // THEN we find the value between the patterns
        assertTrue(match.isPresent());
        assertEquals(ANSWER_1, match.get().trim());
    }

    @Test
    public void getValueBetweenPatterns_singleLine_returnsValue() {
        // GIVEN a single line content
        // WHEN we search between two existing patterns
        Optional<String> match = getValueBetweenPatterns(
            SINGLE_LINE,
            "What is your quest\\?",
            "What is the air-speed velocity of an unladen swallow\\?");

        // THEN we find the value between the patterns
        assertTrue(match.isPresent());
        assertEquals(ANSWER_2, match.get().trim());
    }

    @Test
    public void getValueBetweenPatterns_patternDoesNotExist_returnsEmptyOptional() {
        // GIVEN any content
        // WHEN we search between two non-existent patterns
        Optional<String> match = getValueBetweenPatterns(
            "the content string",
            "this pattern does not exist",
            "neither does this");

        // THEN no value is found
        assertFalse(match.isPresent());
    }

    @Test
    public void getValueAfterPattern_multiLine_returnsValue() {
        // GIVEN a multi line content
        // WHEN we search after an existing pattern
        Optional<String> match = getValueAfterPattern(
            MULTI_LINE,
            "What is the air-speed velocity of an unladen swallow\\?");

        // THEN we find the after the pattern, to the end of the content
        assertTrue(match.isPresent());
        assertEquals(ANSWER_3, match.get().trim());
    }

    @Test
    public void getValueAfterPattern_singleLine_returnsValue() {
        // GIVEN a single line content
        // WHEN we search after an existing pattern
        Optional<String> match = getValueAfterPattern(
            SINGLE_LINE,
            "What is the air-speed velocity of an unladen swallow\\?");

        // THEN we find the after the pattern, to the end of the content
        assertTrue(match.isPresent());
        assertEquals(ANSWER_3, match.get().trim());
    }

    @Test
    public void getValueAfterPattern_patternDoesNotExist_returnsEmptyOptional() {
        // GIVEN any content
        // WHEN we search after a non-existent pattern
        Optional<String> match = getValueAfterPattern(
            "the content string",
            "this pattern does not exist");

        // THEN no value is found
        assertFalse(match.isPresent());
    }
}
