package com.amazon.ata.recommendationsservice.data;

import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import java.util.Arrays;
import java.util.List;

public final class FantasyBookRecommendations {

    private static List<BookRecommendation> recommendations = Arrays.asList(
        new BookRecommendation("The Keeper Chronicles: The Complete Epic Fantasy Trilogy", "JA Andrews",
            "B07QN31TDH"),
        new BookRecommendation("The Witcher Boxed Set: Blood of Elves, The Time of Contempt, Baptism of Fire",
            "Andrzej Sapkowski", "0316438979"),
        new BookRecommendation("The Books of Caledan Trilogy: (An Epic Fantasy Collection: The Tainted Crown, " +
            "The Brooding Crown, The Shattered Crown)", "Meg Cowley", "1692446193"),
        new BookRecommendation("Age of Myth: Book One of The Legends of the First Empire",
            "Michael J. Sullivan", "B0161YQQVK"),
        new BookRecommendation("The Starless Sea: A Novel", "Erin Morgenstern", "B07QBDFB8N"),
        new BookRecommendation("Where the Forest Meets the Stars", "Glendy Vanderah", "B07CWSPSMX"),
        new BookRecommendation("Ninth House (Alex Stern Book 1)", "Leigh Bardugo", "B07LF64DZ2"),
        new BookRecommendation("Soul Weaver (The Seeded Realms Book 1)", "Eric J. Vann", "B07XZDZWG3"),
        new BookRecommendation("Rise of the Dragons (Kings and Sorcerers--Book 1)", "Morgan Rice",
            "B00PQRJJY0"),
        new BookRecommendation("Final Fantasy XV: The Dawn of the Future", "Jun Eishima", "1646090004"));

    private FantasyBookRecommendations() {

    }

    public static List<BookRecommendation> get() {
        return recommendations;
    }
}
