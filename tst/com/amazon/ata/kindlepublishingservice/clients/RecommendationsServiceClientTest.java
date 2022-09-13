package com.amazon.ata.kindlepublishingservice.clients;

import com.amazon.ata.recommendationsservice.types.BookGenre;
//import com.amazon.ata.kindlepublishingservice.metrics.MetricsConstants;
//import com.amazon.ata.kindlepublishingservice.metrics.MetricsPublisher;
import com.amazon.ata.recommendationsservice.RecommendationsService;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import javax.measure.unit.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RecommendationsServiceClientTest {

    @Mock
    private RecommendationsService recommendationsService;

    @InjectMocks
    private RecommendationsServiceClient recommendationsServiceClient;

    @BeforeEach
    public void setup(){
        initMocks(this);
    }

    @Test
    public void getBookRecommendations_validRequest_returnsRecommendations() {
        // GIVEN
        List<BookRecommendation> bookRecommendations = new ArrayList<>();
        BookRecommendation bookRec1 = new BookRecommendation("Book Title 1", "Author 1", "ASIN1");
        BookRecommendation bookRec2 = new BookRecommendation("Book Title 2", "Author 2", "ASIN2");
        bookRecommendations.add(bookRec1);
        bookRecommendations.add(bookRec2);
        when(recommendationsService.getBookRecommendations(BookGenre.MYSTERY)).thenReturn(bookRecommendations);

        // WHEN
        List<BookRecommendation> result = recommendationsServiceClient.getBookRecommendations(BookGenre.MYSTERY);

        // THEN
        assertEquals(bookRecommendations, result, "Expected result to match value returned by RecommendationsService");
    }

    @Test
    public void getBookRecommendations_noRecommendations_returnsEmptyList() {
        // GIVEN
        when(recommendationsService.getBookRecommendations(BookGenre.MYSTERY)).thenReturn(Collections.EMPTY_LIST);

        // WHEN
        List<BookRecommendation> result = recommendationsServiceClient.getBookRecommendations(BookGenre.MYSTERY);

        // THEN
        assertEquals(0, result.size(), "Expected result to be empty.");
    }
}