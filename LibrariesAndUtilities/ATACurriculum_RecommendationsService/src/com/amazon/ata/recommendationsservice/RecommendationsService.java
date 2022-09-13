package com.amazon.ata.recommendationsservice;

import com.amazon.ata.recommendationsservice.data.BookRecommendationData;
import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import org.apache.commons.lang3.RandomUtils;

import java.util.Collections;
import java.util.List;
import javax.inject.Inject;


public class RecommendationsService {

    @Inject
    public RecommendationsService() {

    }
    /**
     * Retrieves three book recommendations based on genre.
     *
     * @param genre of book
     * @return Book recommendations.
     */
    public List<BookRecommendation> getBookRecommendations(BookGenre genre) {

        try {
            List<BookRecommendation> bookRecs = BookRecommendationData.getBookRecommendations(genre);
            Collections.shuffle(bookRecs);
            int sleepMultiplier = RandomUtils.nextInt(5, 10);
            Thread.sleep(1000 * sleepMultiplier);
            return bookRecs.size() > 3 ? bookRecs.subList(0, 3) : bookRecs;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

}
