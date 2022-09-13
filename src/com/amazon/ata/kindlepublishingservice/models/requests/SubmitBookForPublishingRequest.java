package com.amazon.ata.kindlepublishingservice.models.requests;

import java.util.Objects;

public class SubmitBookForPublishingRequest {
    private String bookId;
    private String title;
    private String author;
    private String text;
    private String genre;

    public SubmitBookForPublishingRequest(String bookId, String title, String author, String text, String genre) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.text = text;
        this.genre = genre;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmitBookForPublishingRequest that = (SubmitBookForPublishingRequest) o;
        return Objects.equals(bookId, that.bookId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(author, that.author) &&
                Objects.equals(text, that.text) &&
                genre == that.genre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, author, text, genre);
    }

    public SubmitBookForPublishingRequest(Builder builder) {
        this.bookId = builder.bookId;
        this.title = builder.title;
        this.author = builder.author;
        this.text = builder.text;
        this.genre = builder.genre;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {

        private String bookId;
        private String title;
        private String author;
        private String text;
        private String genre;

        private Builder() {

        }

        public Builder withBookId(String bookIdToUse) {
            this.bookId = bookIdToUse;
            return this;
        }

        public Builder withTitle(String titleToUse) {
            this.title = titleToUse;
            return this;
        }

        public Builder withAuthor(String authorToUse) {
            this.author = authorToUse;
            return this;
        }

        public Builder withText(String textToUse) {
            this.text = textToUse;
            return this;
        }

        public Builder withGenre(String genreToUse) {
            this.genre = genreToUse;
            return this;
        }

        public SubmitBookForPublishingRequest build() { return new SubmitBookForPublishingRequest(this); }
    }
}
