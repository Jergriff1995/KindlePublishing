package com.amazon.ata.kindlepublishingservice.mastery.mt1;

import com.amazon.ata.kindlepublishingservice.App;
import com.amazon.ata.kindlepublishingservice.models.requests.GetBookRequest;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.dagger.ApplicationComponent;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.helpers.IntegrationTestBase;
import com.amazon.ata.kindlepublishingservice.helpers.KindlePublishingServiceTctTestDao.CatalogItemVersion;
import com.amazon.ata.recommendationsservice.types.BookGenre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.testng.Assert.*;

public class MasteryTaskOneTests extends IntegrationTestBase {
    private String bookId;
    private static final ApplicationComponent COMPONENT = App.component;

    @BeforeEach
    public void setupTest() {
        bookId = "MT01_RemoveBookTest_" + UUID.randomUUID();
        System.out.println("Executing RemoveBook test with bookId " + bookId);
    }

    @Test
    public void removeBook_activeCatalogItem_setsToInactive() {
        // GIVEN
        CatalogItemVersion catalogItemVersion = new CatalogItemVersion();
        catalogItemVersion.setBookId(bookId);
        catalogItemVersion.setVersion(1);
        catalogItemVersion.setAuthor("author");
        catalogItemVersion.setGenre(BookGenre.ACTION);
        catalogItemVersion.setText("text");
        catalogItemVersion.setTitle("title");
        catalogItemVersion.setInactive(false);
        super.getTestDao().save(catalogItemVersion);

        RemoveBookFromCatalogRequest removeBookFromCatalogRequest = new RemoveBookFromCatalogRequest();
        removeBookFromCatalogRequest.setBookId(bookId);

        // WHEN
        COMPONENT.provideRemoveBookFromCatalogActivity().execute(removeBookFromCatalogRequest);

        // THEN
        CatalogItemVersion result = super.getTestDao().load(catalogItemVersion);

        assertTrue(result.isInactive(), String.format("Expected catalog item [bookId: %s, version: %s] to be set to " +
            "inactive after the calling the RemoveBook API, but got [%s]",
            catalogItemVersion.getBookId(),
            catalogItemVersion.getVersion(),
            result));
        assertGetBookRequestThrowsBookNotFoundException();
    }

    @Test
    public void removeBook_multipleCatalogItemVersion_marksLatestVersionInactive() {
        // GIVEN a first, inactive version
        CatalogItemVersion firstVersion = new CatalogItemVersion();
        firstVersion.setBookId(bookId);
        firstVersion.setVersion(1);
        firstVersion.setAuthor("author");
        firstVersion.setGenre(BookGenre.ACTION);
        firstVersion.setText("text");
        firstVersion.setTitle("title");
        firstVersion.setInactive(true);
        super.getTestDao().save(firstVersion);
        // and a second, active version
        CatalogItemVersion secondVersion = new CatalogItemVersion(firstVersion);
        secondVersion.setVersion(2);
        secondVersion.setInactive(false);
        super.getTestDao().save(secondVersion);

        RemoveBookFromCatalogRequest removeBookFromCatalogRequest = new RemoveBookFromCatalogRequest();
        removeBookFromCatalogRequest.setBookId(bookId);

        // WHEN we remove the catalog item
        COMPONENT.provideRemoveBookFromCatalogActivity().execute(removeBookFromCatalogRequest);

        // THEN it should only update the second version
        CatalogItemVersion savedSecondVersion = super.getTestDao().load(secondVersion);
        assertTrue(savedSecondVersion.isInactive(), String.format("Expected catalog item [bookId: %s, version: %s]" +
                " to be updated to inactive after calling the RemoveBook API, but got [%s]",
            secondVersion.getBookId(),
            secondVersion.getVersion(),
            savedSecondVersion));

        // and not update the first version
        CatalogItemVersion savedFirstVersion = super.getTestDao().load(firstVersion);
        assertEquals(savedFirstVersion, firstVersion, String.format("Expected earlier catalog item version" +
                "[bookId: %s, version: %s] to not be updated after calling the RemoveBook API.",
            firstVersion.getBookId(),
            firstVersion.getVersion()));

        assertGetBookRequestThrowsBookNotFoundException();
    }

    @Test
    public void removeBook_inactiveCatalogItem_throwsBookNotFoundException() {
        // GIVEN
        CatalogItemVersion catalogItemVersion = new CatalogItemVersion();
        catalogItemVersion.setBookId(bookId);
        catalogItemVersion.setVersion(1);
        catalogItemVersion.setInactive(true);
        super.getTestDao().save(catalogItemVersion);

        RemoveBookFromCatalogRequest removeBookFromCatalogRequest = new RemoveBookFromCatalogRequest();
        removeBookFromCatalogRequest.setBookId(bookId);

        // WHEN + THEN
        assertThrows(BookNotFoundException.class, () ->
                COMPONENT.provideRemoveBookFromCatalogActivity().execute(removeBookFromCatalogRequest));
    }

    @Test
    public void removeBook_itemDoesNotExist_throwsBookNotFoundException() {
        // GIVEN
        RemoveBookFromCatalogRequest removeBookFromCatalogRequest = new RemoveBookFromCatalogRequest();
        removeBookFromCatalogRequest.setBookId(UUID.randomUUID().toString());

        // WHEN + THEN
        assertThrows(BookNotFoundException.class, () ->
                COMPONENT.provideRemoveBookFromCatalogActivity().execute(removeBookFromCatalogRequest));
    }

    private void assertGetBookRequestThrowsBookNotFoundException() {
        GetBookRequest getBookRequest = new GetBookRequest();
        getBookRequest.setBookId(bookId);
        assertThrows(BookNotFoundException.class, () ->
                COMPONENT.provideGetBookActivity().execute(getBookRequest));
    }
}
