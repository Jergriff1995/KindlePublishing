package com.amazon.ata.kindlepublishingservice.models;

import java.util.Objects;

public class BookRecommendation {
    private String title;
    private String author;
    private String asin;

    public BookRecommendation(String title, String author, String asin) {
        this.title = title;
        this.author = author;
        this.asin = asin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookRecommendation that = (BookRecommendation) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(author, that.author) &&
                Objects.equals(asin, that.asin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, asin);
    }

    public BookRecommendation(Builder builder) {
        this.title = builder.title;
        this.author = builder.author;
        this.asin = builder.asin;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private String title;
        private String author;
        private String asin;

        private Builder() {

        }

        public Builder withTitle(String titleToUse) {
            this.title = titleToUse;
            return this;
        }

        public Builder withAuthor(String authorToUse) {
            this.author = authorToUse;
            return this;
        }

        public Builder withAsin(String asinToUse) {
            this.asin = asinToUse;
            return this;
        }

        public BookRecommendation build() { return new BookRecommendation(this); }
    }
}
