package com.amazon.ata.test.helper;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FreeFormTextHelper {
    private FreeFormTextHelper() {
    }

    /**
     * Determines if the given (multiline) content has a match within a single line somewhere in it that corresponds
     * with the given regular expression string.
     *
     * If you contain a dot ({@code .}) in your pattern, it will *not* match newline
     * characters, so your pattern must all be found on a single line of {@code content}. If you need to match a pattern
     * across multiple lines, use {@code matchesMultiLine}.
     *
     * Also note that {@code ^} will only match the beginning of the entire content string; {@code $} will only match
     * the end of the entire content string;
     *
     * @param content The string in which to search for the given regex
     * @param patternToFind The regex to search for within {@code content}.
     * @return If the pattern was found on a single line
     */
    public static boolean matchesSingleLine(final String content, final String patternToFind) {
        Pattern regexPattern = Pattern.compile(patternToFind);
        return regexPattern.matcher(content).find();
    }

    /**
     * Determines if the given (multiline) content has a match that may span lines in it that corresponds with the given
     * regular expression string.
     *
     * If you contain a dot ({@code .}) in your pattern, it *will* match newline characters, so
     * your pattern might be matching across lines. If you intend to match a pattern that should only exist on a
     * single line, use {@code matchesSingleLine}.
     *
     * Also note that {@code ^} will match beginning of *any* line; {@code $} will match end of *any* line.
     *
     * @param content The string in which to search for the given regex
     * @param patternToFind The regex to search for within {@code content}.
     * @return If the pattern was found across multiple lines
     */
    public static boolean matchesMultiLine(final String content, final String patternToFind) {
        // DOTALL allows . to match newline
        // MULTILINE allows ^/$ to match beginning/end of *any* line, not just beginning/end of entire content string
        Pattern regexPattern = Pattern.compile(patternToFind, Pattern.DOTALL | Pattern.MULTILINE);
        return regexPattern.matcher(content).find();
    }

    /**
     * Searches a provided String content for a section of text in between
     * the two provided patterns.
     * <p>
     * Useful for things like getting the answer in between two known text patterns
     * (e.g. the answer between question 1 and question 2).
     * <p>
     * NOTE: The input patterns get compiled to a {@link java.util.regex.Pattern},
     * so regex is fair game, or make sure to escape any unintentional regex characters!
     *
     * @param leftPattern  The start pattern to search between.
     *                     Gets compiled into a {@link java.util.regex.Pattern}.
     * @param rightPattern The end pattern to search between.
     *                     Gets compiled into a {@link java.util.regex.Pattern}.
     * @param content      The String to search through.
     * @return             An Optional String with the contents between the start and end pattern.
     *                     Empty if no match was found.
     */
    public static Optional<String> getValueBetweenPatterns(final String content,
                                                           final String leftPattern,
                                                           final String rightPattern) {
        // DOTALL allows . to match newline
        // CASE_INSENSITIVE for convenience, but can consider removing
        Pattern questionPattern = Pattern.compile(String.format("%s(.*?)%s", leftPattern, rightPattern),
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher answerMatcher = questionPattern.matcher(content);

        if (answerMatcher.find()) {
            return Optional.of(answerMatcher.group(1));
        }

        return Optional.empty();
    }

    /**
     * Searches a provided String content for a section of text from a start pattern
     * to the end of the String.
     * <p>
     * NOTE: The input pattern gets compiled to a {@link java.util.regex.Pattern},
     * so regex is fair game, or make sure to escape any unintentional regex characters!
     *
     * @param leftPattern  The left regex to search between.
     *                     Will be compiled into a {@link java.util.regex.Pattern}.
     * @param content      The String to search through.
     * @return             An Optional String with the contents after the start pattern.
     *                     Empty if no match was found.
     */
    public static Optional<String> getValueAfterPattern(final String content, final String leftPattern) {
        return getValueBetweenPatterns(content, leftPattern, "\\z");
    }
}
