package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.recommendationsservice.types.BookGenre;

/**
 * Kindle formatted book POJO - fields have been formatted for the kindle.
 */
public class KindleFormattedBook {
    private String bookId;
    private String title;
    private String author;
    private String text;
    private BookGenre genre;

    private KindleFormattedBook(KindleFormattedBook.Builder builder) {
        this.bookId = builder.bookId;
        this.title = builder.title;
        this.author = builder.author;
        this.text = builder.text;
        this.genre = builder.genre;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public BookGenre getGenre() {
        return genre;
    }

    /**
     * Creates a builder object to aid in KindleFormattedBook object creation.
     * @return the builder to create KindleFormattedBook objects
     */
    public static KindleFormattedBook.Builder builder() {
        return new KindleFormattedBook.Builder();
    }

    /**
     * KindleFormattedBook builder static inner class.
     */
    public static final class Builder {
        private String bookId;
        private String title;
        private String author;
        private String text;
        private BookGenre genre;

        /**
         * Builder constructor.
         */
        //CHECKSTYLE:OFF:HiddenField
        public Builder() {
        }

        /**
         *  Sets the bookId and returns a reference to this Builder to the methods can be chained
         *  together.
         *
         * @param bookId The book id to set.
         * @return a reference to this Builder.
         */
        public KindleFormattedBook.Builder withBookId(String bookId) {
            this.bookId = bookId;
            return this;
        }

        /**
         *  Sets the title and returns a reference to this Builder to the methods can be chained
         *  together.
         *
         * @param title The title to set.
         * @return a reference to this Builder.
         */
        public KindleFormattedBook.Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         *  Sets the author and returns a reference to this Builder to the methods can be chained
         *  together.
         *
         * @param author The author to set.
         * @return a reference to this Builder.
         */
        public KindleFormattedBook.Builder withAuthor(String author) {
            this.author = author;
            return this;
        }

        /**
         *  Sets the text and returns a reference to this Builder to the methods can be chained
         *  together.
         *
         * @param text The text to set.
         * @return a reference to this Builder.
         */
        public KindleFormattedBook.Builder withText(String text) {
            this.text = text;
            return this;
        }

        /**
         *  Sets the genre and returns a reference to this Builder to the methods can be chained
         *  together.
         *
         * @param genre The genre to set.
         * @return a reference to this Builder.
         */
        public KindleFormattedBook.Builder withGenre(BookGenre genre) {
            this.genre = genre;
            return this;
        }

        /**
         *  Returns a KindleFormattedBook built from the parameters previously set.
         *
         * @return a KindleFormattedBook with parameters of this Builder.
         */
        public KindleFormattedBook build() {
            return new KindleFormattedBook(this);
        }
    }
}
