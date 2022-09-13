package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatalogCoralConverterTest {

    @Test
    public void toCoral_returnsBook() {
        // GIVEN
        CatalogItemVersion item = new CatalogItemVersion();
        item.setBookId("book.123");
        item.setVersion(1);
        item.setInactive(false);
        item.setGenre(BookGenre.FANTASY);
        item.setTitle("Title");
        item.setText("Text");
        item.setAuthor("Author");

        // WHEN
        Book book = CatalogItemConverter.toBook(item);

        // THEN
        assertEquals(item.getBookId(), book.getBookId(), "Book Id field not converted properly.");
        assertEquals(item.getVersion(), book.getVersion(), "Version field not converted properly.");
        assertEquals(item.getTitle(), book.getTitle(), "Title field not converted properly.");
        assertEquals(item.getAuthor(), book.getAuthor(), "Author field not converted properly.");
        assertEquals(item.getText(), book.getText(), "Text field not converted properly.");
        assertEquals(item.getGenre().name(), book.getGenre(), "Genre field not converted properly.");
    }
}