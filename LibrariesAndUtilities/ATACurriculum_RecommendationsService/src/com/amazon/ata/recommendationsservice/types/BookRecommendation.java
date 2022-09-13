package com.amazon.ata.recommendationsservice.types;

public class BookRecommendation {
    private String title;
    private String author;
    private String asin;

    /**
     * Book Recommendation Constructor.
     *
     * @param title of book.
     * @param author of book.
     * @param asin of book.
     */
    public BookRecommendation(String title, String author, String asin) {
        this.title = title;
        this.author = author;
        this.asin = asin;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAsin() {
        return asin;
    }

    /**
     * Display string.
     * @return display string.
     */
    public String toString() {
        return String.format("%s by %s (asin: %s)", title, author, asin);
    }
}
