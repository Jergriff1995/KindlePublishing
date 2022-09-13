package com.amazon.ata.kindlepublishingservice.models.requests;

import java.util.Objects;

public class RemoveBookFromCatalogRequest {
    private String bookId;

    public RemoveBookFromCatalogRequest() {}

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveBookFromCatalogRequest that = (RemoveBookFromCatalogRequest) o;
        return Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }

    public RemoveBookFromCatalogRequest(Builder builder) {
        this.bookId = builder.bookId;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private String bookId;

        private Builder() {

        }

        public Builder withBookId(String bookIdToUse) {
            this.bookId = bookIdToUse;
            return this;
        }

        public RemoveBookFromCatalogRequest build() { return new RemoveBookFromCatalogRequest(this); }
    }
}
