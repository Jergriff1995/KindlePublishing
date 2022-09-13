package com.amazon.ata.kindlepublishingservice.models;

import java.util.Objects;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String text;
    private String genre;
    private int version;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Book(String bookId, String title, String author, String text, String genre, int version) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.text = text;
        this.genre = genre;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return version == book.version &&
                Objects.equals(bookId, book.bookId) &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(text, book.text) &&
                genre == book.genre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, author, text, genre, version);
    }

    public static Builder builder() {return new Builder();}

    public Book(Builder builder) {
        this.bookId = builder.bookId;
        this.title = builder.title;
        this.author = builder.author;
        this.text = builder.text;
        this.genre = builder.genre;
        this.version = builder.version;
    }

    public static final class Builder {
        private String bookId;
        private String title;
        private String author;
        private String text;
        private String genre;
        private int version;

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

        public Builder withVersion(int versionToUse) {
            this.version = versionToUse;
            return this;
        }

        public Book build() { return new Book(this); }
    }
}
