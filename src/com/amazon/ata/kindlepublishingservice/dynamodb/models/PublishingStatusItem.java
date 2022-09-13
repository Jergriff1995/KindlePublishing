package com.amazon.ata.kindlepublishingservice.dynamodb.models;

import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

@DynamoDBTable(tableName = "PublishingStatus")
public class PublishingStatusItem {
    private String publishingRecordId;
    private PublishingRecordStatus status;
    private String statusMessage;
    private String bookId;

    @DynamoDBHashKey(attributeName = "publishingRecordId")
    public String getPublishingRecordId() {
        return publishingRecordId;
    }

    public void setPublishingRecordId(String publishingRecordId) {
        this.publishingRecordId = publishingRecordId;
    }

    @DynamoDBTypeConvertedEnum
    @DynamoDBRangeKey(attributeName = "status")
    public PublishingRecordStatus getStatus() {
        return status;
    }

    public void setStatus(PublishingRecordStatus status) {
        this.status = status;
    }

    @DynamoDBAttribute(attributeName = "statusMessage")
    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @DynamoDBAttribute(attributeName = "bookId")
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
