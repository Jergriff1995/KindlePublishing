package com.amazon.ata.recommendationsservice.data;

import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import java.util.Arrays;
import java.util.List;

public final class TravelBookRecommendations {

    private static List<BookRecommendation> recommendations = Arrays.asList(
        new BookRecommendation("The Bucket List: 1000 Adventures Big & Small",
        "Kath Stathers", "0789332698"),
        new BookRecommendation("World Travel: An Irreverent Guide",
        "Anthony Bourdain", "0062802798"),
        new BookRecommendation("Destinations of a Lifetime: 225 of the World's Most Amazing Places",
            "National Geographic", "1426215649"),
        new BookRecommendation("The Twenty-Ninth Day: Surviving a Grizzly Attack in the Canadian Tundra",
            "Alex Messenger", "B07QQVDG8P"),
        new BookRecommendation("The Travel Book: A Journey Through Every Country in the World (Lonely Planet)",
            "Lonely Planet", "178657120X"),
        new BookRecommendation("On the Plain of Snakes: A Mexican Journey",
            "Anthony Bourdain", "0062802798"),
        new BookRecommendation("World Travel: An Irreverent Guide",
            "Paul Theroux", "0544866479"),
        new BookRecommendation("Where To Go When: Unforgettable Trips for Every Month (DK Eyewitness Travel Guide)",
            "DK Eyewitness", "146549409X"),
        new BookRecommendation("100 Parks, 5,000 Ideas: Where to Go, When to Go, What to See, What to Do",
            "Joe Yogerst", "1426220103"),
        new BookRecommendation("Destination Earth: A New Philosophy of Travel by a World-Traveler",
            "Nicos Hadjicostis", "0997414804"));


    private TravelBookRecommendations() {

    }

    public static List<BookRecommendation> get() {
        return recommendations;
    }
}
