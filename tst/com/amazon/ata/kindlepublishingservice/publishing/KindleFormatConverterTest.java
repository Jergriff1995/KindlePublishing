package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.recommendationsservice.types.BookGenre;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class KindleFormatConverterTest {

    @Test
    public void format_bookIdPresent_allFieldsPopulated() {
        // GIVEN
        BookPublishRequest request = BookPublishRequest.builder()
            .withBookId("bookId")
            .withTitle("Title")
            .withAuthor("Author")
            .withGenre(BookGenre.FANTASY)
            .withText("This is a story.")
            .withPublishingRecordId("publishing.123")
            .build();

        // WHEN
        KindleFormattedBook formattedBook = KindleFormatConverter.format(request);

        // THEN
        assertEquals(request.getGenre(), formattedBook.getGenre(), "Expected genre to be unchanged.");
        assertEquals(request.getBookId(), formattedBook.getBookId(), "Expected bookId to be unchanged.");

        assertNotNull(formattedBook.getTitle(), "Expected title to be present in formatted book.");
        assertNotNull(formattedBook.getAuthor(), "Expected author to be present in formatted book.");
        assertNotNull(formattedBook.getText(), "Expected text to be present in formatted book.");
    }

    @Test
    public void format_bookIdNotPresent_allFieldsPopulated() {
        // GIVEN
        BookPublishRequest request = BookPublishRequest.builder()
            .withTitle("Title")
            .withAuthor("Author")
            .withGenre(BookGenre.FANTASY)
            .withText("This is a story.")
            .withPublishingRecordId("publishing.123")
            .build();

        // WHEN
        KindleFormattedBook formattedBook = KindleFormatConverter.format(request);

        // THEN
        assertEquals(request.getGenre(), formattedBook.getGenre(), "Expected genre to be unchanged.");

        assertNull(formattedBook.getBookId(), "Expected bookId to not be populated.");

        assertNotNull(formattedBook.getTitle(), "Expected title to be present in formatted book.");
        assertNotNull(formattedBook.getAuthor(), "Expected author to be present in formatted book.");
        assertNotNull(formattedBook.getText(), "Expected text to be present in formatted book.");
    }
}
