package com.amazon.ata.test.assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentest4j.AssertionFailedError;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.amazon.ata.test.assertions.AtaAssertions.assertClose;
import static com.amazon.ata.test.assertions.AtaAssertions.assertContains;
import static com.amazon.ata.test.assertions.AtaAssertions.assertContainsBetween;
import static com.amazon.ata.test.assertions.AtaAssertions.assertMatchesMultiLine;
import static com.amazon.ata.test.assertions.AtaAssertions.assertMatchesSingleLine;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AtaAssertionsTest {

    // assertContains

    @ParameterizedTest
    @ValueSource(strings = {"two", "One", " three", "e\n two"})
    void assertContains_stringMatches_noAssertionsFire(String matchingPattern) {
        // GIVEN -- matching pattern:
        //   - string in middle, beginning, end
        //   - string includes newline

        // WHEN + THEN -- no assert failure
        assertContains("One\n two three", matchingPattern, "");
    }

    @Test
    void assertContains_subStringNotContained_assertFires() {
        assertThrows(AssertionFailedError.class, () -> assertContains("One two", "three ", ""));
    }

    // assertContainsBetween

    private static Stream<Arguments> provideStringsForAssertContains() {
        return Stream.of(
            Arguments.of("One\n", "three"),
            Arguments.of("two", "four\n"),
            Arguments.of("One\n", "four\n"),
            Arguments.of("One\n", "five"),
            Arguments.of("O", "e")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringsForAssertContains")
    void assertContains_stringContainedBetweenPatterns_noAssertionsFire(String left, String right) {
        // GIVEN -- matching pattern:
        //   - string between substrings in middle of content
        //   - start and end patterns includes newline
        //   - string between start and end of content


        // WHEN + THEN -- no assert failure
        assertContainsBetween("One\n two three four\n five", left, right, "");
    }

    @Test
    void assertContains_stringNotContainedBetweenPatterns_assertFires() {
        assertThrows(AssertionFailedError.class, () -> assertContainsBetween("One two", "One", "four", ""));
    }

    @Test
    void assertContains_emptyStringContainedBetweenPatterns_assertFires() {
        assertThrows(AssertionFailedError.class, () -> assertContainsBetween("One two", "One", "two", ""));
    }

    // assertMatchesSingleLine

    @ParameterizedTest
    @ValueSource(strings = {"cd", "c.e"})
    void assertMatchesSingleLine_patternMatches_noAssertFires(String matchingPattern) {
        // GIVEN -- matching pattern:
        //   - simple string pattern
        //   - wildcard that matches

        // WHEN + THEN -- no assert failure
        assertMatchesSingleLine("ab\ncde\nfg", matchingPattern, "");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd", "ac+d", "a.*d"})
    void assertMatchesSingleLine_patternDoesntMatch_assertFires(String nonMatchingPattern) {
        // GIVEN -- non-matching pattern:
        //   - simple string not in content
        //   - wildcard that doesn't match
        //   - wildcard that *spans* lines

        // WHEN + THEN -- assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertMatchesSingleLine("ab\ncde\nfg", nonMatchingPattern, "")
        );
    }

    // assertMatchesMultiLine

    @ParameterizedTest
    @ValueSource(strings = {"cd", "c.e", "a.*d", "^c.e*"})
    void assertMatchesMultiLine_patternMatches_noAssertFires(String matchingPattern) {
        // GIVEN -- matching pattern
        //   - simple string single line
        //   - wildcard on single line
        //   - wildcard across lines
        //   - matching beginning/end of internal line

        // WHEN + THEN -- no assert failure
        assertMatchesMultiLine("ab\ncde\nfg", matchingPattern, "");
    }

    @ParameterizedTest
    @ValueSource(strings = {"gh", "ac+d"})
    void assertMatchesMultiLine_patternDoesntMatch_assertFires(String nonMatchingPattern) {
        // GIVEN -- non-matching pattern
        //   - simple string single line
        //   - wildcard

        // WHEN + THEN -- assert failure
        assertThrows(
            AssertionFailedError.class,
            () -> assertMatchesMultiLine("ab\ncde\nfg", nonMatchingPattern, "")
        );
    }

    // assertClose(expected, actual, message)

    @Test
    void assertClose_withNullExpected_throwsException() {
        // GIVEN - null expected, non-null actual, margin
        // WHEN + THEN - assert failure
        assertThrows(
            IllegalArgumentException.class,
            () -> assertClose(null, BigDecimal.ONE, ""),
            "Expected assertBigDecimal(null, ...) to throw IAE, but did not"
        );
    }

    @Test
    void assertClose_withNullActual_throwsException() {
        // GIVEN - non-null expected, null actual, non-null margin
        // WHEN + THEN - assert failure
        assertThrows(
            IllegalArgumentException.class,
            () -> assertClose(BigDecimal.ONE, null, ""),
            "Expected assertBigDecimal(..., null, ...) to throw IAE, but did not"
        );
    }

    @Test
    void assertClose_withLessThanOnePercentDifference_noAssertFires() {
        // GIVEN - expected greater than actual
        BigDecimal expected = BigDecimal.TEN;
        BigDecimal actual = expected.multiply(new BigDecimal("1.009"));

        // WHEN + THEN - no assert failure
        assertClose(expected, actual, "");
    }

    @Test
    void assertClose_withMoreThanOnePercentDifference_assertFires() {
        // GIVEN - expected greater than actual
        BigDecimal expected = BigDecimal.TEN;
        BigDecimal actual = expected.multiply(new BigDecimal("1.011"));

        // WHEN + THEN - no assert failure
        assertThrows(AssertionFailedError.class, () -> assertClose(expected, actual, ""));
    }

    @Test
    void assertClose_withSmallDifferenceAndNegativeExpected_noAssertFires() {
        // GIVEN - expected greater than actual
        BigDecimal expected = BigDecimal.TEN.negate();
        BigDecimal actual = expected.multiply(new BigDecimal("1.009"));

        // WHEN + THEN - no assert failure
        assertClose(expected, actual, "");
    }

    @Test
    void assertClose_exactlyEqual_noAssertFires() {
        // GIVEN - expected greater than actual
        BigDecimal expected = BigDecimal.TEN;
        BigDecimal actual = BigDecimal.TEN;

        // WHEN + THEN - no assert failure
        assertClose(expected, actual, "");
    }

    // assertClose(expected, actual, margin, message)

    @Test
    void assertClose_withMargin_withNullExpected_throwsException() {
        // GIVEN - null expected, non-null actual, margin
        // WHEN + THEN - assert failure
        assertThrows(
            IllegalArgumentException.class,
            () -> assertClose(null, BigDecimal.ONE, BigDecimal.ONE, ""),
            "Expected assertBigDecimal(null, ...) to throw IAE, but did not"
        );
    }

    @Test
    void assertClose_withMargin_withNullActual_throwsException() {
        // GIVEN - non-null expected, null actual, non-null margin
        // WHEN + THEN - assert failure
        assertThrows(
            IllegalArgumentException.class,
            () -> assertClose(BigDecimal.ONE, null, BigDecimal.ONE, ""),
            "Expected assertBigDecimal(..., null, ...) to throw IAE, but did not"
        );
    }

    @Test
    void assertClose_withMargin_withNullMargin_throwsException() {
        // GIVEN - non-null expected, non-null actual, null margin
        // WHEN + THEN - assert failure
        assertThrows(
            IllegalArgumentException.class,
            () -> assertClose(BigDecimal.ONE, BigDecimal.ONE, null, ""),
            "Expected assertBigDecimal(..., null) to throw IAE, but did not"
        );
    }

    @Test
    void assertClose_withMargin_withNegativeMargin_throwsException() {
        // GIVEN - non-null expected, non-null actual
        // negative margin
        BigDecimal negative = BigDecimal.ONE.negate();

        // WHEN + THEN - assert failure
        assertThrows(
            IllegalArgumentException.class,
            () -> assertClose(BigDecimal.ONE, BigDecimal.ONE, negative, ""),
            "Expected assertBigDecimal(..., negativeMargin) to throw IAE, but did not"
        );
    }

    @Test
    void assertClose_withMargin_withLargerExpectedAndDiffBelowMargin_noAssertFires() {
        // GIVEN - expected greater than actual
        BigDecimal expected = new BigDecimal("10.1");
        BigDecimal actual = BigDecimal.TEN;
        // margin larger than diff
        BigDecimal margin = new BigDecimal("0.11");

        // WHEN + THEN - no assert failure
        assertClose(expected, actual, margin, "");
    }

    @Test
    void assertClose_withMargin_withSmallerExpectedAndDiffBelowMargin_noAssertFires() {
        // GIVEN - expected smaller than actual
        BigDecimal expected = new BigDecimal("9.9");
        BigDecimal actual = BigDecimal.TEN;
        // margin larger than diff
        BigDecimal margin = new BigDecimal("0.11");

        // WHEN + THEN - no assert failure
        assertClose(expected, actual, margin, "");
    }

    @Test
    void assertClose_withMargin_withLargerExpectedAndDiffAboveMargin_assertFires() {
        // GIVEN - expected greater than actual
        BigDecimal expected = new BigDecimal("10.1");
        BigDecimal actual = BigDecimal.TEN;
        // margin larger than diff
        BigDecimal margin = new BigDecimal("0.01");

        // WHEN + THEN - assert failure
        assertThrows(AssertionFailedError.class, () -> assertClose(expected, actual, margin, ""));
    }

    @Test
    void assertClose_withMargin_withSmallerExpectedAndDiffAboveMargin_assertFires() {
        // GIVEN - expected smaller than actual
        BigDecimal expected = new BigDecimal("9.9");
        BigDecimal actual = BigDecimal.TEN;
        // margin larger than diff
        BigDecimal margin = new BigDecimal("0.01");

        // WHEN + THEN - assert failure
        assertThrows(AssertionFailedError.class, () -> assertClose(expected, actual, margin, ""));
    }

    @Test
    void assertClose_withMargin_exactlyEqual_noAssertFires() {
        // GIVEN - expected greater than actual
        BigDecimal expected = BigDecimal.TEN;
        BigDecimal actual = BigDecimal.TEN;

        // WHEN + THEN - no assert failure
        assertClose(expected, actual, new BigDecimal("0.001"), "");
    }
}
