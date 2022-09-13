package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.kindlepublishingservice.models.requests.SubmitBookForPublishingRequest;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

/**
 * Converters for BookPublishRequest related objects.
 */
public class BookPublishRequestConverter {

    private BookPublishRequestConverter() {}

    /**
     * Converts the given {@link SubmitBookForPublishingRequest} object to a {@link BookPublishRequest}. Generates
     * a publishing record id.
     *
     * @param request The SubmitBookForPublishing object to convert.
     * @return The converted BookPublishRequest.
     */
    public static BookPublishRequest toBookPublishRequest(SubmitBookForPublishingRequest request) {

        final String publishingRecordId = KindlePublishingUtils.generatePublishingRecordId();

        return BookPublishRequest.builder()
            .withPublishingRecordId(publishingRecordId)
            .withText(request.getText())
            .withTitle(request.getTitle())
            .withBookId(request.getBookId())
            .withGenre(BookGenre.valueOf(request.getGenre()))
            .withAuthor(request.getAuthor())
            .build();
    }

}
