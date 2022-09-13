package com.amazon.ata.kindlepublishingservice.models.requests;

import java.util.Objects;

public class GetPublishingStatusRequest {
    private String publishingRecordId;

    public GetPublishingStatusRequest(String publishingRecordId) {
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
        GetPublishingStatusRequest that = (GetPublishingStatusRequest) o;
        return publishingRecordId.equals(that.publishingRecordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publishingRecordId);
    }

    public GetPublishingStatusRequest(Builder builder) {
        this.publishingRecordId = builder.publishingRecordId;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private String publishingRecordId;

        private Builder() {

        }

        public Builder withPublishingRecordId(String publishingRecordIdToUse) {
            this.publishingRecordId = publishingRecordIdToUse;
            return this;
        }

        public GetPublishingStatusRequest build() { return new GetPublishingStatusRequest(this); }
    }
}
