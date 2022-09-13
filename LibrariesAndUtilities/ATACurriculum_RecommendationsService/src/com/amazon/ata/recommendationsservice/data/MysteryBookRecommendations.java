package com.amazon.ata.recommendationsservice.data;

import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import java.util.Arrays;
import java.util.List;

public final class MysteryBookRecommendations {

    private static List<BookRecommendation> recommendations = Arrays.asList(
        new BookRecommendation("Where the Crawdads Sing", "Delia Owens", "0735219095"),
        new BookRecommendation("My Sister's Grave (Tracy Crosswhite Book 1)", "Robert Dugoni", "B00K2EOONI"),
        new BookRecommendation("Then She Was Gone: A Novel", "Lisa Jewell", "B07BQFXLFN"),
        new BookRecommendation("Ordinary Grace", "William Kent Krueger", "1451645856"),
        new BookRecommendation("The Address: A Novel", "Fiona Davis", "B01MU2K87X"),
        new BookRecommendation("The Life We Bury", "Allen Eskens", "1494513889"),
        new BookRecommendation("Foreign Deceit (David Wolf Book 1)", "Jeff Carson", "B00AR5HWXQ"),
        new BookRecommendation("The Couple Next Door: A Novel", "Shari Lapena", "0735221103"),
        new BookRecommendation("The Outsider", "Stephen King", "B079586RDT"),
        new BookRecommendation("The Tuscan Child", "Rhys Bowen", "B074QL7WNM"));

    private MysteryBookRecommendations() {

    }

    public static List<BookRecommendation> get() {
        return recommendations;
    }

}

