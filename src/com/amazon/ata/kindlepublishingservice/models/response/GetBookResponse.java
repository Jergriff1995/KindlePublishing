package com.amazon.ata.kindlepublishingservice.models.response;

import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.models.BookRecommendation;

import java.util.List;
import java.util.Objects;

public class GetBookResponse {
    private Book book;
    private List<BookRecommendation> recommendations;

    public GetBookResponse(Book book, List<BookRecommendation> recommendations) {
        this.book = book;
        this.recommendations = recommendations;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<BookRecommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<BookRecommendation> recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetBookResponse that = (GetBookResponse) o;
        return Objects.equals(book, that.book) &&
                Objects.equals(recommendations, that.recommendations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, recommendations);
    }

    public GetBookResponse(Builder builder) {
        this.book = builder.book;
        this.recommendations = builder.recommendations;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private Book book;
        private List<BookRecommendation> recommendations;

        private Builder() {

        }

        public Builder withBook(Book book) {
            this.book = book;
            return this;
        }

        public Builder withRecommendations(List<BookRecommendation> recommendationsToUse) {
            this.recommendations = recommendationsToUse;
            return this;
        }

        public GetBookResponse build() { return new GetBookResponse(this); }
    }
}
