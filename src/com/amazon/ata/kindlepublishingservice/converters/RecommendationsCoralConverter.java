package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.coral.converter.CoralConverterUtil;
import com.amazon.ata.kindlepublishingservice.models.BookRecommendation;

import java.util.List;

/**
 * Converters for Recommendations related objects.
 */
public class RecommendationsCoralConverter {

    private RecommendationsCoralConverter() {}

    /**
     * Converts the given BookRecommendations list into the corresponding Coral BookRecommendationsList object.
     *
     * @param bookRecommendations BookRecommendations list to convert.
     * @return Coral BookRecommendations list.
     */
    public static List<BookRecommendation> toCoral(List<com.amazon.ata.recommendationsservice.types.BookRecommendation>
                                                       bookRecommendations) {
        return CoralConverterUtil.convertList(bookRecommendations, RecommendationsCoralConverter::toCoral);
    }

    /**
     * Converts the given BookRecommendations object to the corresponding Coral BookRecommendation object.
     * @param bookRecommendation BookRecommendation object to convert
     * @return Coral BookRecommendation object.
     */
    public static BookRecommendation toCoral(com.amazon.ata.recommendationsservice.types.BookRecommendation
                                                 bookRecommendation) {
        return BookRecommendation.builder()
            .withAsin(bookRecommendation.getAsin())
            .withAuthor(bookRecommendation.getAuthor())
            .withTitle(bookRecommendation.getTitle())
            .build();
    }
}
