package com.amazon.ata.recommendationsservice.data;

import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import java.util.Arrays;
import java.util.List;

public final class CookingBookRecommendations {

    private static List<BookRecommendation> recommendations = Arrays.asList(
        new BookRecommendation("Salt, Fat, Acid, Heat: Mastering the Elements of Good Cooking", "Samin Nosrat",
            "1476753830"),
        new BookRecommendation("The Complete Cooking for Two Cookbook, Gift Edition: 650 Recipes for Everything " +
            "You'll Ever Want to Make", "America's Test Kitchen", "1945256060"),
        new BookRecommendation("The Pioneer Woman Cooks: The New Frontier: 112 Fantastic Favorites for Everyday " +
            "Eating", "Ree Drummond", "0062561375"),
        new BookRecommendation("Half Baked Harvest Super Simple: More Than 125 Recipes for Instant, Overnight, " +
            "Meal-Prepped, and Easy Comfort Foods: A Cookbook", "Tieghan Gerard", "0525577076"),
        new BookRecommendation("Magnolia Table: A Collection of Recipes for Gathering", "Joanna Gaines",
            "006282015X"),
        new BookRecommendation("The Complete Mediterranean Cookbook: 500 Vibrant, Kitchen-Tested Recipes for " +
            "Living and Eating Well Every Day", "America's Test Kitchen", "1940352649"),
        new BookRecommendation("Nothing Fancy: Unfussy Food for Having People Over", "Alison Roman",
            "0451497015"),
        new BookRecommendation("Cravings: Recipes for All the Food You Want to Eat: A Cookbook",
            "Chrissy Teigen", "1101903910"),
        new BookRecommendation("The Blue Zones Kitchen: 100 Recipes to Live to 100", "Dan Buettner",
            "1426220138"),
        new BookRecommendation("Budget Bytes: Over 100 Easy, Delicious Recipes to Slash Your Grocery Bill in " +
            "Half: A Cookbook", "Beth Moncel", "1583335307"));

    private CookingBookRecommendations() {

    }

    public static List<BookRecommendation> get() {
        return recommendations;
    }
}
