package com.amazon.ata.recommendationsservice.data;

import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import java.util.Arrays;
import java.util.List;

public final class RomanceBookRecommendations {

    private static List<BookRecommendation> recommendations = Arrays.asList(
        new BookRecommendation("Winter Cottage", "Mary Ellen Taylor", "B079JQ29M5"),
        new BookRecommendation("Falling For You: A Sweet Small Town Romance (Sapphire Bay Book 1)",
            "Leeanna Morgan", "B07CW4CHWV"),
        new BookRecommendation("When We Believed in Mermaids: A Novel", "Barbara O'Neal", "B07MV8SWZF"),
        new BookRecommendation("Where the Forest Meets the Stars", "Glendy Vanderah", "B07CWSPSMX"),
        new BookRecommendation("The Overdue Life of Amy Byler", "Kelly Harms", "B07GVK5HW8"),
        new BookRecommendation("The Art of Inheriting Secrets: A Novel", "Barbara O'Neal", "B078X9D4H5"),
        new BookRecommendation("Verity", "Colleen Hoover", "B07HJYTRMD"),
        new BookRecommendation("Regretting You", "Colleen Hoover", "B07SH5V2NB"),
        new BookRecommendation("The Giver of Stars: A Novel", "Jojo Moyes", "B07NKP3JL4"),
        new BookRecommendation("The Empty Nesters", "Carolyn Brown", "B07MM54C3R"));

    private RomanceBookRecommendations() {

    }

    public static List<BookRecommendation> get() {
        return recommendations;
    }
}
