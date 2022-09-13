package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishing.utils.KindleConversionUtils;

/**
 * Contains methods that help convert a book to its Kindle format.
 */
public final class KindleFormatConverter {

    private KindleFormatConverter(){}

    /**
     * Formats the book fields in the provided publishRequest into their kindle format version.
     * @param publishRequest contains information about a book to be published
     * @return returns a kindle formatted book
     */
    public static KindleFormattedBook format(BookPublishRequest publishRequest) {
        return KindleFormattedBook.builder()
            .withText(KindleConversionUtils.convertTextToKindleFormat(publishRequest.getText()))
            .withAuthor(publishRequest.getAuthor())
            .withTitle(publishRequest.getTitle())
            .withGenre(publishRequest.getGenre())
            .withBookId(publishRequest.getBookId())
            .build();
    }
}
