package com.amazon.ata.recommendationsservice.data;

import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import java.util.Arrays;
import java.util.List;

public final class ScienceFictionBookRecommendations {
    private static List<BookRecommendation> recommendations = Arrays.asList(
        new BookRecommendation("Dune", "Frank Herbert", "B00B7NPRY8"),
        new BookRecommendation("1984: New Classic Edition", "George Orwell", "B000Q6ZLOI"),
        new BookRecommendation("Winter World (The Long Winter Trilogy Book 1)", "A.G. Riddle", "B07N32K12H"),
        new BookRecommendation("The Stand", "Stephen King", "B0078XQQIC"),
        new BookRecommendation("Fahrenheit 451", "Ray Bradbury", "1451673310"),
        new BookRecommendation("Parable of the Sower", "Octavia E. Butler", "B008HALO4Q"),
        new BookRecommendation("The Jekyll Revelation", "Robert Masello", "B010JG1YFO"),
        new BookRecommendation("Ready Player One", "Ernest Cline", "B004J4WKUQ"),
        new BookRecommendation("Leviathan Wakes", "James S. A. Corey", "B073HBQXMT"),
        new BookRecommendation("Brave New World", "Aldous Huxley", "0060850523"));

    private ScienceFictionBookRecommendations() {

    }

    public static List<BookRecommendation> get() {
        return recommendations;
    }
}
