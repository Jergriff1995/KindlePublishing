package com.amazon.ata.kindlepublishingservice.utils;

import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;

import com.google.common.collect.ImmutableMap;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

public class KindlePublishingUtils {

    private static Map<PublishingRecordStatus, String> statusToMessage = ImmutableMap.of(
        PublishingRecordStatus.QUEUED, "Queued for publishing at %s",
        PublishingRecordStatus.IN_PROGRESS, "Processing started at %s",
        PublishingRecordStatus.SUCCESSFUL, "Book published at %s",
        PublishingRecordStatus.FAILED, "Book publish failed at %s"
    );

    /**
     * Generates a book id to save into the database.
     *
     * @return The generated book id.
     */
    public static String generateBookId() {
        return "book." + UUID.randomUUID();
    }

    /**
     * Generates a publishing record id to save into the database.
     *
     * @return The generated publishing record id.
     */
    public static String generatePublishingRecordId() {
        return "publishingstatus." + UUID.randomUUID();
    }

    /**
     * Generates the publishing status message for a given PublishingRecordStatus.
     *
     * @param status The publishing record status.
     * @return The corresponding publishing status message.
     */
    public static String generatePublishingStatusMessage(PublishingRecordStatus status) {

        return String.format(statusToMessage.get(status), new Timestamp(System.currentTimeMillis()));
    }
}
