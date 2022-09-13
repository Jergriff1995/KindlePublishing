package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Accesses the Publishing Status table.
 */
public class PublishingStatusDao {

    private static final String ADDITIONAL_NOTES_PREFIX = " Additional Notes: ";
    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new PublishingStatusDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the publishing status table.
     */
    @Inject
    public PublishingStatusDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Updates the publishing status table for the given publishingRecordId with the provided
     * publishingRecordStatus. If the bookId is provided it will also be stored in the record.
     *
     * @param publishingRecordId The id of the record to update
     * @param publishingRecordStatus The PublishingRecordStatus to save into the table.
     * @param bookId The id of the book associated with the request, may be null
     * @return The stored PublishingStatusItem.
     */
    public PublishingStatusItem setPublishingStatus(String publishingRecordId,
                                                    PublishingRecordStatus publishingRecordStatus,
                                                    String bookId) {
        return setPublishingStatus(publishingRecordId, publishingRecordStatus, bookId, null);
    }
    public List<PublishingStatusRecord> getListOfStatus(String publishingRecordId) {

        //create an object to reflect an entry in the table
        PublishingStatusItem status = new PublishingStatusItem();

        //set its ID to the one in the request
        status.setPublishingRecordId(publishingRecordId);

        // create a query for matching IDs
        DynamoDBQueryExpression<PublishingStatusItem> queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(status);

        // get a list containing table entries (full entries, NOT just PublishingStatusRecord)
        List<PublishingStatusItem> results = dynamoDbMapper.query(PublishingStatusItem.class, queryExpression);

        // case for no table entries being found.
        if (results.isEmpty()) {
            throw new PublishingStatusNotFoundException("Publishing Status not found");
        }

        // now we need a list to hold the PublishingStatusItem
        List<PublishingStatusRecord> listOfStatus = new ArrayList<>();

        // loop though the list of table entries
        for(PublishingStatusItem tableEntry : results){

        // create a new PublishingStatusRecord with the status, message, and bookID from the given table entry
            PublishingStatusRecord recordToAdd = new PublishingStatusRecord(tableEntry.getStatus().toString(), tableEntry.getStatusMessage(),
                    tableEntry.getBookId());

        // add our new PublishingStatusRecord to the list
            listOfStatus.add(recordToAdd);

        }

        // return the list
        return listOfStatus;
    }

    /**
     * Updates the publishing status table for the given publishingRecordId with the provided
     * publishingRecordStatus. If the bookId is provided it will also be stored in the record. If
     * a message is provided, it will be appended to the publishing status message in the datastore.
     *
     * @param publishingRecordId The id of the record to update
     * @param publishingRecordStatus The PublishingRecordStatus to save into the table.
     * @param bookId The id of the book associated with the request, may be null
     * @param message additional notes stored with the status
     * @return The stored PublishingStatusItem.
     */
    public PublishingStatusItem setPublishingStatus(String publishingRecordId,
                                                    PublishingRecordStatus publishingRecordStatus,
                                                    String bookId,
                                                    String message) {
        String statusMessage = KindlePublishingUtils.generatePublishingStatusMessage(publishingRecordStatus);
        if (StringUtils.isNotBlank(message)) {
            statusMessage = new StringBuffer()
                .append(statusMessage)
                .append(ADDITIONAL_NOTES_PREFIX)
                .append(message)
                .toString();
        }

        PublishingStatusItem item = new PublishingStatusItem();
        item.setPublishingRecordId(publishingRecordId);
        item.setStatus(publishingRecordStatus);
        item.setStatusMessage(statusMessage);
        item.setBookId(bookId);
        dynamoDbMapper.save(item);
        return item;
    }
}
