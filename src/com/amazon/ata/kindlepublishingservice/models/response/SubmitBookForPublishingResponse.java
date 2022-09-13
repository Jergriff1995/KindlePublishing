package com.amazon.ata.kindlepublishingservice.models.response;

import java.util.Objects;

public class SubmitBookForPublishingResponse {
    private String publishingRecordId;

    public SubmitBookForPublishingResponse(String publishingRecordId) {
        this.publishingRecordId = publishingRecordId;
    }

    public String getPublishingRecordId() {
        return publishingRecordId;
    }

    public void setPublishingRecordId(String publishingRecordId) {
        this.publishingRecordId = publishingRecordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmitBookForPublishingResponse that = (SubmitBookForPublishingResponse) o;
        return Objects.equals(publishingRecordId, that.publishingRecordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publishingRecordId);
    }

    public static Builder builder() {return new Builder();}

    public SubmitBookForPublishingResponse(Builder builder) {
        this.publishingRecordId = builder.publishingRecordId;
    }

    public static final class Builder {
        private String publishingRecordId;

        private Builder() {

        }

        public Builder withPublishingRecordId(String publishingRecordIdToUse) {
            this.publishingRecordId = publishingRecordIdToUse;
            return this;
        }

        public SubmitBookForPublishingResponse build() { return new SubmitBookForPublishingResponse(this); }
    }
}
