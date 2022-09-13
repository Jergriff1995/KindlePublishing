package com.amazon.ata.music.playlist.service.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public final class MusicPlaylistServiceUtils {
    private static final Pattern INVALID_CHARACTER_PATTERN = Pattern.compile("[\"\'\\\\]");
    // package private for testing
    static final int PLAYLIST_ID_LENGTH = 5;

    // do not instantiate
    private MusicPlaylistServiceUtils() {}

    /**
     * Checks that the provided String contains only valid characters.
     *
     * @param stringToValidate the playlist name to be validated
     * @return true if the String is valid (contains only valid characters),
     *         false otherwise
     */
    public static boolean isValidString(final String stringToValidate) {
        if (StringUtils.isBlank(stringToValidate)) {
            return false;
        }

        return !INVALID_CHARACTER_PATTERN.matcher(stringToValidate).find();
    }

    public static String generatePlaylistId() {
        return RandomStringUtils.randomAlphanumeric(PLAYLIST_ID_LENGTH);
    }
}
