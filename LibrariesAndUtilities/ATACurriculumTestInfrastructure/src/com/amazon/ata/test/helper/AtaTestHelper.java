package com.amazon.ata.test.helper;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Provides methods useful in executing validating correctness of project tasks.
 */
public class AtaTestHelper {

    private AtaTestHelper() {
    }

    /**
     * Gets the contents of the provided file name. If the file cannot be found an IllegalArgumentException is thrown.
     *
     * @param filename the name of the resource to get the contents of, relative to a source root
     *                 (e.g. start the path inside .../[Brazil package root]/src)
     * @return the contents of the provided filename as a String
     */
    public static String getFileContentFromResources(String filename) {
        InputStream is;
        String content = null;
        try {
            is = AtaTestHelper.class.getClassLoader().getResourceAsStream(filename);
            content = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Unable to find file: %s.", filename), e);
        }
        return content;
    }

    /**
     * Handles the reporting for a test failing due to unexpected exception (they tend to lack information for our
     * TCTs).
     *
     * @param e The throwable that occurred
     * @param failureSummary Summary of the error condition that occurred (begins the message reported in the
     *                       assertion failure
     */
    public static void failTestWithException(Throwable e, String failureSummary) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String failMessage = String.format(
            "%s. Failed with %s: %s%n%s",
            failureSummary,
            e.getClass().getSimpleName(),
            e.getMessage(),
            stringWriter.toString());
        fail(failMessage);
    }

    /**
     * @deprecated  - Moved to {@link FreeFormTextHelper}.
     *
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
    @Deprecated
    public static boolean matchesSingleLine(final String content, final String patternToFind) {
        Pattern regexPattern = Pattern.compile(patternToFind);
        return regexPattern.matcher(content).find();
    }

    /**
     * @deprecated - Moved to {@link FreeFormTextHelper}.
     *
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
    @Deprecated
    public static boolean matchesMultiLine(final String content, final String patternToFind) {
        // DOTALL allows . to match newline
        // MULTILINE allows ^/$ to match beginning/end of *any* line, not just beginning/end of entire content string
        Pattern regexPattern = Pattern.compile(patternToFind, Pattern.DOTALL | Pattern.MULTILINE);
        return regexPattern.matcher(content).find();
    }
}
