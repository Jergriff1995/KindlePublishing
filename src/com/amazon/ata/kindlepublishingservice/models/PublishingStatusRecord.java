package com.amazon.ata.kindlepublishingservice.models;

import java.util.Objects;

public class PublishingStatusRecord {
    private String status;
    private String statusMessage;
    private String bookId;

    public PublishingStatusRecord(String status, String statusMessage, String bookId) {
        this.status = status;
        this.statusMessage = statusMessage;
        this.bookId = bookId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

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
        PublishingStatusRecord that = (PublishingStatusRecord) o;
        return status == that.status &&
                Objects.equals(statusMessage, that.statusMessage) &&
                Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, statusMessage, bookId);
    }

    public static Builder builder() {return new Builder();}

    public PublishingStatusRecord(Builder builder) {
        this.status = builder.status;
        this.statusMessage = builder.statusMessage;
        this.bookId = builder.bookId;
    }

    public static final class Builder {
        private String status;
        private String statusMessage;
        private String bookId;

        private Builder() {

        }

        public Builder withStatus(String statusToUse) {
            this.status = statusToUse;
            return this;
        }

        public Builder withStatusMessage(String statusMessageToUse) {
            this.statusMessage = statusMessageToUse;
            return this;
        }

        public Builder withBookId(String bookIdToUse) {
            this.bookId = bookIdToUse;
            return this;
        }

        public PublishingStatusRecord build() { return new PublishingStatusRecord(this); }
    }
}
