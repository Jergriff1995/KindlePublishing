package com.amazon.ata.kindlepublishing.utils;

/**
 * Utility methods that help convert a book to it's Kindle format.
 */
public final class KindleConversionUtils {

    private KindleConversionUtils(){}

    /**
     * Accepts the text of the book and coverts it to the kindle text format.
     * @param text the text to format
     * @return the formatted text
     */
    public static String convertTextToKindleFormat(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Cannot convert null text.");
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            return text;
        }

        return text;
    }
}
