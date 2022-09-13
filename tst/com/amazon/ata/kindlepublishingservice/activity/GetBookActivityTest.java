package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.models.requests.GetBookRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetBookResponse;
import com.amazon.ata.kindlepublishingservice.clients.RecommendationsServiceClient;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetBookActivityTest {

    private static String BOOK_ID = "book.123";
    private static final String TITLE = "Title of Book";
    private static final String AUTHOR = "Book Author";
    private static final String ASIN = "B123456789";

    @Mock
    private CatalogDao catalogDao;

    @Mock
    private RecommendationsServiceClient recommendationsServiceClient;

    @InjectMocks
    private GetBookActivity activity;

    @BeforeEach
    public void setup(){
        initMocks(this);
    }

    @Test
    public void execute_bookExists_returnsBook() {
        // GIVEN
        GetBookRequest request = GetBookRequest
                .builder()
                .withBookId(BOOK_ID)
                .build();

        CatalogItemVersion catalogItem = new CatalogItemVersion();
        catalogItem.setVersion(1);
        catalogItem.setBookId(BOOK_ID);
        catalogItem.setInactive(false);
        catalogItem.setGenre(BookGenre.FANTASY);

        List<BookRecommendation> bookRecommendations = new ArrayList<>();
        bookRecommendations.add(new BookRecommendation(TITLE, AUTHOR, ASIN));
        when(recommendationsServiceClient.getBookRecommendations(BookGenre.FANTASY)).thenReturn(bookRecommendations);
        when(catalogDao.getBookFromCatalog(BOOK_ID)).thenReturn(catalogItem);

        // WHEN
        GetBookResponse response = activity.execute(request);

        // THEN
        assertNotNull(response, "Expected request to return a non-null response.");
        assertNotNull(response.getBook(), "Expected a non null book in the response.");
        Book book = response.getBook();
        assertEquals(BOOK_ID, book.getBookId(), "Expected book in response to contain id passed in request.");
        assertNotNull(response.getRecommendations(), "Expected non null book recommendations in the response.");
        assertEquals(TITLE, response.getRecommendations().get(0).getTitle(), "Expected recommendations in the " +
                "response to match recommendations returned by recommendations service.");
    }

    @Test
    public void execute_bookDoesNotExist_throwsException() {
        // GIVEN
        GetBookRequest request = GetBookRequest
                .builder()
                .withBookId("notAbook.123")
                .build();

        when(catalogDao.getBookFromCatalog("notAbook.123")).thenThrow(new BookNotFoundException("No book found"));

        // WHEN & THEN
        assertThrows(BookNotFoundException.class, () -> activity.execute(request), "Expected activity to " +
                "throw an exception if the book can't be found.");
    }
}