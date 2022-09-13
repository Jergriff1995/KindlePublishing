package com.amazon.ata.kindlepublishingservice.models.response;

import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;

import java.util.List;
import java.util.Objects;

public class GetPublishingStatusResponse {
    private List<PublishingStatusRecord> publishingStatusHistory;

    public GetPublishingStatusResponse(List<PublishingStatusRecord> publishingStatusHistory) {
        this.publishingStatusHistory = publishingStatusHistory;
    }

    public List<PublishingStatusRecord> getPublishingStatusHistory() {
        return publishingStatusHistory;
    }

    public void setPublishingStatusHistory(List<PublishingStatusRecord> publishingStatusHistory) {
        this.publishingStatusHistory = publishingStatusHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetPublishingStatusResponse that = (GetPublishingStatusResponse) o;
        return Objects.equals(publishingStatusHistory, that.publishingStatusHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publishingStatusHistory);
    }

    public GetPublishingStatusResponse(Builder builder) {
        this.publishingStatusHistory = builder.publishingStatusHistory;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private List<PublishingStatusRecord> publishingStatusHistory;

        private Builder() {

        }

        public Builder withPublishingStatusHistory(List<PublishingStatusRecord> publishingStatusHistoryToUse) {
            this.publishingStatusHistory = publishingStatusHistoryToUse;
            return this;
        }

        public GetPublishingStatusResponse build() { return new GetPublishingStatusResponse(this); }
    }
}
