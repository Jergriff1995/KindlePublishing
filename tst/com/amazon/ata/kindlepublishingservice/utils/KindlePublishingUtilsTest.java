package com.amazon.ata.kindlepublishingservice.utils;

import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KindlePublishingUtilsTest {

    @Test
    public void generateBookId_containsBookPrefix() {
        // GIVEN

        // WHEN
        String bookId = KindlePublishingUtils.generateBookId();

        // THEN
        assertTrue(bookId.startsWith("book."), String.format("Expected a bookId to be prefixed with: 'book.', " +
            "instead received '%s'.", bookId));
    }

    @Test
    public void generatePublishingRecordId_containsPublishingStatusPrefix() {
        // GIVEN

        // WHEN
        String publishingRecordId = KindlePublishingUtils.generatePublishingRecordId();

        // THEN
        assertTrue(publishingRecordId.startsWith("publishingstatus."), String.format("Expected a publishingRecordId " +
            "to be prefixed with: 'publishingstatus.', instead received '%s'", publishingRecordId));
    }

    @Test
    public void generatePublishingStatusMessage_queuedStatus_messageGenerated() {
        // GIVEN

        // WHEN
        String message = KindlePublishingUtils.generatePublishingStatusMessage(PublishingRecordStatus.QUEUED);

        // THEN
        assertTrue(message.startsWith("Queued"), "Expected message to start with 'Queued'.");
    }

    @Test
    public void generatePublishingStatusMessage_inProgressStatus_messageGenerated() {
        // GIVEN

        // WHEN
        String message = KindlePublishingUtils.generatePublishingStatusMessage(PublishingRecordStatus.IN_PROGRESS);

        // THEN
        assertTrue(message.startsWith("Processing started"), "Expected message to start with " +
            "'Processing started'.");
    }

    @Test
    public void generatePublishingStatusMessage_successfulStatus_messageGenerated() {
        // GIVEN

        // WHEN
        String message = KindlePublishingUtils.generatePublishingStatusMessage(PublishingRecordStatus.SUCCESSFUL);

        // THEN
        assertTrue(message.startsWith("Book published"), "Expected message to start with 'Book published'.");
    }

    @Test
    public void generatePublishingStatusMessage_failedStatus_messageGenerated() {
        // GIVEN

        // WHEN
        String message = KindlePublishingUtils.generatePublishingStatusMessage(PublishingRecordStatus.FAILED);

        // THEN
        assertTrue(message.startsWith("Book publish failed"),"Expected message to start with 'Book publish " +
            "failed'.");
    }
}
