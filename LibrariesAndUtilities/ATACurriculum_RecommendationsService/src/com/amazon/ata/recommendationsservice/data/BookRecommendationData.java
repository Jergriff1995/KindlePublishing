package com.amazon.ata.recommendationsservice.data;

import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BookRecommendationData {

    private static Map<BookGenre, List<BookRecommendation>> genreToRecs = new HashMap<BookGenre,
        List<BookRecommendation>>() {
        {
            put(BookGenre.TRAVEL, TravelBookRecommendations.get());
            put(BookGenre.FANTASY, FantasyBookRecommendations.get());
            put(BookGenre.ROMANCE, RomanceBookRecommendations.get());
            put(BookGenre.MYSTERY, MysteryBookRecommendations.get());
            put(BookGenre.SCIENCE_FICTION, ScienceFictionBookRecommendations.get());
            put(BookGenre.COOKING, CookingBookRecommendations.get());
            put(BookGenre.AUTOBIOGRAPHY, AutobiographyBookRecommendations.get());
        }
    };

    private BookRecommendationData() {

    }

    public static List<BookRecommendation> getBookRecommendations(BookGenre genre) {
        return genreToRecs.getOrDefault(genre, Collections.emptyList());
    }

}
