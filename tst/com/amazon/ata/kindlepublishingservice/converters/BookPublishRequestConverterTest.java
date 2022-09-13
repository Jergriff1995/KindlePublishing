package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.kindlepublishingservice.models.requests.SubmitBookForPublishingRequest;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookPublishRequestConverterTest {

    @Test
    public void toBookPublishRequest_requestProvided_returnsMatchingRequest() {
        // GIVEN
        SubmitBookForPublishingRequest submitRequest = SubmitBookForPublishingRequest.builder()
            .withGenre("FANTASY")
            .withBookId("book.123")
            .withTitle("Title")
            .withAuthor("Author")
            .withText("Text")
            .build();

        // WHEN
        BookPublishRequest bookPublishRequest = BookPublishRequestConverter.toBookPublishRequest(submitRequest);

        // THEN
        assertEquals(submitRequest.getGenre(), bookPublishRequest.getGenre().name(), "Genre field not converted properly.");
        assertEquals(submitRequest.getBookId(), bookPublishRequest.getBookId(), "Book Id field not converted properly.");
        assertEquals(submitRequest.getTitle(), bookPublishRequest.getTitle(), "Title field not converted properly.");
        assertEquals(submitRequest.getAuthor(), bookPublishRequest.getAuthor(), "Author field not converted properly.");
        assertEquals(submitRequest.getText(), bookPublishRequest.getText(), "Text field not converted properly.");
    }

    @Test
    public void toBookPublishRequest_requestProvided_publishingIdPresent() {
        // GIVEN
        SubmitBookForPublishingRequest submitRequest = SubmitBookForPublishingRequest.builder()
            .withGenre("FANTASY")
            .withBookId("book.123")
            .withTitle("Title")
            .withAuthor("Author")
            .withText("Text")
            .build();

        // WHEN
        BookPublishRequest bookPublishRequest = BookPublishRequestConverter.toBookPublishRequest(submitRequest);

        // THEN
        assertNotNull(bookPublishRequest.getPublishingRecordId(), "Expected converter to create a publishing" +
            "record ID.");
    }
}
