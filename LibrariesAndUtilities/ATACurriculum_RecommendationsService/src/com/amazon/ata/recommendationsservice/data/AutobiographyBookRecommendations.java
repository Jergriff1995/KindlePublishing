package com.amazon.ata.recommendationsservice.data;

import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import java.util.Arrays;
import java.util.List;

public final class AutobiographyBookRecommendations {

    private static List<BookRecommendation> recommendations = Arrays.asList(
        new BookRecommendation("It's a Long Story: My Life", "Willie Nelson", "B00UJZX7CO"),
        new BookRecommendation("Becoming", "Michelle Obama", "B07B3JQZCL"),
        new BookRecommendation("If You Ask Me: (And of Course You Won't)", "Betty White", "B004Z76OOU"),
        new BookRecommendation("Cash: The Autobiography", "Johnny Cash", "0060727535"),
        new BookRecommendation("Me: Elton John Official Autobiography", "Elton John", "1250147603"),
        new BookRecommendation("I Can't Make This Up: Life Lessons", "Kevin Hart", "B06VX4GGFT"),
        new BookRecommendation("Whiskey in a Teacup: What Growing Up in the South Taught Me About Life, Love, " +
            "and Baking Biscuits", "Reese Witherspoon", "1501166271"),
        new BookRecommendation("The Collected Autobiographies of Maya Angelou (Modern Library (Hardcover))",
            "Maya Angelou", "0679643257"),
        new BookRecommendation("A Life in Parts", "Bryan Cranston", "B01F7MT78Q"));

    private AutobiographyBookRecommendations() {

    }

    public static List<BookRecommendation> get() {
        return recommendations;
    }
}
