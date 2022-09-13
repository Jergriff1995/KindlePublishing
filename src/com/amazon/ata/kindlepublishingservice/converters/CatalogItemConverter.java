package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;

/**
 * Converters for Catalog related objects.
 */
public class CatalogItemConverter {

    private CatalogItemConverter() {}

    /**
     * Converts the given {@link CatalogItemVersion} object into the corresponding Coral Book object.
     *
     * @param catalogItem CatalogItem item to convert.
     * @return Book Coral Book object.
     */
    public static Book toBook(CatalogItemVersion catalogItem) {
        return Book.builder()
            .withBookId(catalogItem.getBookId())
            .withVersion(catalogItem.getVersion())
            .withAuthor(catalogItem.getAuthor())
            .withTitle(catalogItem.getTitle())
            .withText(catalogItem.getText())
            .withGenre(catalogItem.getGenre().name())
            .build();
    }
}
