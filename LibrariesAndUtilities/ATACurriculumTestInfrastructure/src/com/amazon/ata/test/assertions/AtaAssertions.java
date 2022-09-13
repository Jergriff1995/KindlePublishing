package com.amazon.ata.test.assertions;

import com.amazon.ata.test.helper.AtaTestHelper;

import org.junit.platform.commons.util.StringUtils;

import java.math.BigDecimal;

import static com.amazon.ata.test.helper.FreeFormTextHelper.getValueBetweenPatterns;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Additional assertion methods for use in TCTs.
 */
public final class AtaAssertions {

    private AtaAssertions() {
    }

    /**
     * Asserts that the given content contains the substring provided.
     *
     * @param content The string in which to search for the given substring
     * @param expectedString The substring to search for in {@code content}
     * @param message The failure message to display if substring not found.
     */
    public static void assertContains(final String content, final String expectedString, final String message) {
        assertTrue(content.contains(expectedString), message);
    }

    /**
     * Asserts that the given content contains non-null, non-empty, non-whitespace text
     * between the provided patterns.
     *
     * @param content      The string in which to search between provided patterns
     * @param startPattern The start pattern to search between.
     *                     Gets compiled into a {@link java.util.regex.Pattern}.
     * @param endPattern   The end pattern to search between.
     *                     Gets compiled into a {@link java.util.regex.Pattern}.
     * @param message      The failure message to display if text petween patterns is null, empty, or whitespace only
     */
    public static void assertContainsBetween(final String content,
                                             final String startPattern,
                                             final String endPattern,
                                             final String message) {
        assertTrue(getValueBetweenPatterns(content, startPattern, endPattern).isPresent(), message);
        assertTrue(StringUtils.isNotBlank(getValueBetweenPatterns(content, startPattern, endPattern).get()),
            message);

    }

    /**
     * Asserts that the given (multiline) content has a match within a single line somewhere in it that corresponds
     * with the given regular expression string.
     *
     * If you contain a dot ({@code .}) in your pattern, it will *not* match newline
     * characters, so your pattern must all be found on a single line of {@code content}. If you need to match a pattern
     * across multiple lines, use {@code assertMatchesMultiLine}.
     *
     * Also note that {@code ^} will only match the beginning of the entire content string; {@code $} will only match
     * the end of the entire content string;
     *
     * @param content The string in which to search for the given regex
     * @param patternToFind The regex to search for within {@code content}.
     * @param message The failure message to display if pattern not found in {@code content}
     */
    public static void assertMatchesSingleLine(final String content, final String patternToFind, final String message) {
        assertTrue(AtaTestHelper.matchesSingleLine(content, patternToFind), message);
    }

    /**
     * Asserts that the given (multiline) content has a match that may span lines in it that corresponds with the given
     * regular expression string.
     *
     * If you contain a dot ({@code .}) in your pattern, it *will* match newline characters, so
     * your pattern might be matching across lines. If you intend to match a pattern that should only exist on a
     * single line, use {@code assertMatchesSingleLine}.
     *
     * Also note that {@code ^} will match beginning of *any* line; {@code $} will match end of *any* line.
     *
     * @param content The string in which to search for the given regex
     * @param patternToFind The regex to search for within {@code content}.
     * @param message The failure message to display if pattern not found in {@code content}
     */
    public static void assertMatchesMultiLine(final String content, final String patternToFind, final String message) {
        assertTrue(AtaTestHelper.matchesMultiLine(content, patternToFind), message);
    }

    /**
     * Asserts that {@code expected} and {@code actual} are "close": that {@code actual}
     * is within 1% of {@code expected}.
     *
     * If you would like to specify your own margin of error (e.g. comparing to zero),
     * consider using the {@code assertClose(BigDecimal, BigDecimal, BigDecimal, String)}
     * version of this method.
     *
     * @param expected The expected BigDecimal value
     * @param actual The actual BigDecimal to compare
     * @param message The error message attached to the assertion fail if expected, actual are not "close"
     */
    public static void assertClose(BigDecimal expected, BigDecimal actual, String message) {
        if (null == expected) {
            throw new IllegalArgumentException("expected cannot be null");
        }
        assertClose(expected, actual, expected.multiply(new BigDecimal("0.01")).abs(), message);
    }

    /**
     * Asserts that {@code expected} and {@code actual} are "close", as defined by being within {@code margin}
     * of one another.
     *
     * {@code expected}, {@code actual}, and {@code margin} must all be non-null.
     * {@code margin} must be non-negative.
     *
     * @param expected The expected BigDecimal value
     * @param actual The actual BigDecimal to compare
     * @param margin The maximum distance between expected, actual for them to be "close" enough
     * @param message The error message attached to the assertion fail if expected, actual are not "close"
     */
    public static void assertClose(
            BigDecimal expected,
            BigDecimal actual,
            BigDecimal margin,
            String message) {

        if (null == expected) {
            throw new IllegalArgumentException("expected cannot be null");
        }
        if (null == actual) {
            throw new IllegalArgumentException("actual cannot be null");
        }
        if (null == margin) {
            throw new IllegalArgumentException("margin cannot be null");
        }
        if (margin.compareTo(new BigDecimal("0")) < 0) {
            throw new IllegalArgumentException("Margin cannot be less than zero: " + margin);
        }
        assertTrue(expected.subtract(actual).abs().compareTo(margin) <= 0, message);
    }
}
