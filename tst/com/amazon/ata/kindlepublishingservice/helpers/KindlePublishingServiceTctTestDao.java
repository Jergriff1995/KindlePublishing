package com.amazon.ata.kindlepublishingservice.helpers;


import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * DAO to create or read test data without needing a corresponding API.
 */
public class KindlePublishingServiceTctTestDao {
    private final List<KindlePublishingServiceTctTestData> itemsToCleanup =
        Collections.synchronizedList(new ArrayList<>());
    private DynamoDBMapper dynamoDbMapper;

    /**
     * constructor.
     * @param dynamoDbMapper the dynamoDbMapper
     */
    public KindlePublishingServiceTctTestDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Save an instance of a KindlePublishingServiceTctTestData class.
     * @param item the item to save
     * @param <T> the type of item to save
     * @return the saved item
     */
    public <T extends KindlePublishingServiceTctTestData> T save(T item) {
        this.dynamoDbMapper.save(item);
        this.itemsToCleanup.add(item);
        return item;
    }

    /**
     * Load an instance of a KindlePublishingServiceTctTestData class.
     * @param key the key object
     * @param <T> the type of the loaded item
     * @return the loaded item from dynamoDB
     */
    public <T extends KindlePublishingServiceTctTestData> T load(T key) {
        return this.dynamoDbMapper.load(key);
    }

    /**
     * Clean up the data we've saved using this DAO
     *
     * package private to limit usage.
     *
     * Threadsafe in case we're running tests in parallel. If so, it's important call this method
     * in the @AfterSuite annotation when all tests are done. Otherwise you
     * risk deleting running tests' data!
     */
    void cleanup() {
        System.out.println(String.format("Cleaning up test data, wrote [%s] items to DynamoDB.",
            itemsToCleanup.size()));

        synchronized (itemsToCleanup) {
            List<DynamoDBMapper.FailedBatch> failures = this.dynamoDbMapper.batchDelete(itemsToCleanup);
            for (DynamoDBMapper.FailedBatch failure : failures) {
                System.out.println("Failed to clean up a batch of test data! Exception: " + failure.getException());
                System.out.println("Failed to clean up items: " + failure.getUnprocessedItems());
                System.out.println("If this continues, please create a CQA and include this output.");
                System.out.println(failure.getUnprocessedItems());
            }
        }
    }

    /**
     * Common interface of what this DAO can save.
     * private because there shouldn't be many types of things.
     */
    private interface KindlePublishingServiceTctTestData {
    }

    /**
     * Copy of com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion.
     *
     * icky copying these over from the service,
     * but participants are not asked to modify the model and
     * I prefer this over working with the low level DDB API and raw values
     */
    @DynamoDBTable(tableName = "CatalogItemVersions")
    public static class CatalogItemVersion implements KindlePublishingServiceTctTestData {
        private String bookId;
        private int version;
        private boolean inactive;
        private String title;
        private String author;
        private String text;
        private BookGenre genre;

        /**
         * Default constructor.
         */
        public CatalogItemVersion() { }

        /**
         * Copy constructor.
         * @param copy the CatalogItemVersion to copy from.
         */
        public CatalogItemVersion(final CatalogItemVersion copy) {
            this.bookId = copy.bookId;
            this.version = copy.version;
            this.inactive = copy.inactive;
            this.title = copy.title;
            this.author = copy.author;
            this.text = copy.text;
            this.genre = copy.genre;
        }

        @DynamoDBHashKey(attributeName = "bookId")
        public String getBookId() {
            return bookId;
        }

        public void setBookId(String bookId) {
            this.bookId = bookId;
        }

        @DynamoDBRangeKey(attributeName = "version")
        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        @DynamoDBAttribute(attributeName = "title")
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @DynamoDBAttribute(attributeName = "author")
        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        @DynamoDBAttribute(attributeName = "text")
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @DynamoDBTypeConvertedEnum
        @DynamoDBAttribute(attributeName = "genre")
        public BookGenre getGenre() {
            return genre;
        }

        public void setGenre(BookGenre genre) {
            this.genre = genre;
        }

        @DynamoDBAttribute(attributeName = "inactive")
        public boolean isInactive() {
            return inactive;
        }

        public void setInactive(boolean active) {
            inactive = active;
        }

        @Override
        public String toString() {
            return "CatalogItemVersion{" +
                "bookId='" + bookId + '\'' +
                ", version=" + version +
                ", inactive=" + inactive +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                ", genre=" + genre +
                '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CatalogItemVersion)) {
                return false;
            }
            CatalogItemVersion that = (CatalogItemVersion) o;
            return getVersion() == that.getVersion() &&
                isInactive() == that.isInactive() &&
                Objects.equals(getBookId(), that.getBookId()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getAuthor(), that.getAuthor()) &&
                Objects.equals(getText(), that.getText()) &&
                getGenre() == that.getGenre();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getBookId(),
                getVersion(),
                isInactive(),
                getTitle(),
                getAuthor(),
                getText(),
                getGenre());
        }
    }

    /**
     * Copy of com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem.
     */
    @DynamoDBTable(tableName = "PublishingStatus")
    public static class PublishingStatusItem implements KindlePublishingServiceTctTestData {
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

        @Override
        public String toString() {
            return "PublishingStatusItem{" +
                "publishingRecordId='" + publishingRecordId + '\'' +
                ", status=" + status +
                ", statusMessage='" + statusMessage + '\'' +
                ", bookId='" + bookId + '\'' +
                '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PublishingStatusItem)) {
                return false;
            }
            PublishingStatusItem that = (PublishingStatusItem) o;
            return Objects.equals(getPublishingRecordId(), that.getPublishingRecordId()) &&
                getStatus() == that.getStatus() &&
                Objects.equals(getStatusMessage(), that.getStatusMessage()) &&
                Objects.equals(getBookId(), that.getBookId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getPublishingRecordId(), getStatus(), getStatusMessage(), getBookId());
        }
    }

    /**
     * Copy of com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus.
     */
    public enum PublishingRecordStatus {
        QUEUED,
        IN_PROGRESS,
        FAILED,
        SUCCESSFUL;
    }
}
