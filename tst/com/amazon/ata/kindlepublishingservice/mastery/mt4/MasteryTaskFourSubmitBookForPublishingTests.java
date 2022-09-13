package com.amazon.ata.kindlepublishingservice.mastery.mt4;

import com.amazon.ata.kindlepublishingservice.dagger.ATAKindlePublishingServiceManager;
import com.amazon.ata.kindlepublishingservice.dagger.ApplicationComponent;
import com.amazon.ata.kindlepublishingservice.dagger.DaggerApplicationComponent;
import com.amazon.ata.kindlepublishingservice.helpers.IntegrationTestBase;
import com.amazon.ata.kindlepublishingservice.helpers.KindlePublishingServiceTctTestDao.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.helpers.SubmitBookForPublishingHelper;
import com.amazon.ata.kindlepublishingservice.models.*;
import com.amazon.ata.kindlepublishingservice.models.requests.GetBookRequest;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.requests.SubmitBookForPublishingRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetBookResponse;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazon.ata.kindlepublishingservice.models.response.SubmitBookForPublishingResponse;
import com.amazon.ata.recommendationsservice.types.BookGenre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.Assert.*;

public class MasteryTaskFourSubmitBookForPublishingTests extends IntegrationTestBase {
    private static final Duration GET_EXPECTED_STATUS_BUFFER = Duration.ofMillis(500L);
    private static final int MAX_GET_EXPECTED_STATUS_ATTEMPTS = 10;
    private static final ApplicationComponent COMPONENT = DaggerApplicationComponent.create();

    /**
     * Ensure the test infra is ready for test run, including creating the client.
     */
    @BeforeEach
    public void setup() {
        ATAKindlePublishingServiceManager publishingManager = COMPONENT.provideATAKindlePublishingServiceManager();
        try {
            publishingManager.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void submitBookForPublishing_noBookId_publishesAndCreatesNewBook() {
        // GIVEN
        SubmitBookForPublishingRequest request = SubmitBookForPublishingRequest.builder()
            .withAuthor("author")
            .withGenre(String.valueOf(BookGenre.TRAVEL))
            .withText("text")
            .withTitle("title")
            .build();

        // WHEN
        SubmitBookForPublishingResponse response = COMPONENT.provideSubmitBookForPublishingActivity().execute(request);

        // THEN
        // wait for queued status
        waitForExpectedStatus(response.getPublishingRecordId(),
            PublishingStatus.QUEUED);
        // wait for in progress status
        waitForExpectedStatus(response.getPublishingRecordId(),
            PublishingStatus.IN_PROGRESS);

        SubmitBookForPublishingHelper.waitForPublishing();

        // wait for successful status
        PublishingStatusRecord successful = waitForExpectedStatus(response.getPublishingRecordId(),
            PublishingStatus.SUCCESSFUL);

        assertNotNull(successful.getBookId(), "A successful PublishingStatusRecord should contain a bookId.");

        // a book should exist now
        GetBookRequest getBookRequest = GetBookRequest.builder()
            .withBookId(successful.getBookId())
            .build();

        GetBookResponse getBookResponse = COMPONENT.provideGetBookActivity().execute(getBookRequest);

        Book book = getBookResponse.getBook();
        assertEquals(book.getAuthor(), request.getAuthor(), "Expected a successful book publish request" +
            "to create a book with the provided author");
        assertEquals(book.getBookId(), successful.getBookId(), "Expected a successful book publish request" +
            "to create a book with the request's book ID");
        assertEquals(book.getGenre(), request.getGenre(), "Expected a successful book publish request" +
            "to create a book with the provided genre");
        assertEquals(book.getText(), request.getText(), "Expected a successful book publish request" +
            "to create a book with the provided text");
        assertEquals(book.getTitle(), request.getTitle(), "Expected a successful book publish request" +
            "to create a book with the provided title");
        assertEquals(book.getVersion(), 1, "Expected a successful book publish request" +
            "to create a new book with version 1");
    }

    @Test
    public void submitBookForPublishing_existingBookId_publishesAndCreatesNewBookVersion() {
        // GIVEN
        String bookId = "book." + UUID.randomUUID().toString();
        SubmitBookForPublishingRequest request = SubmitBookForPublishingRequest.builder()
            .withAuthor("author")
            .withBookId(bookId)
            .withGenre(String.valueOf(BookGenre.TRAVEL))
            .withText("text")
            .withTitle("title")
            .build();

        CatalogItemVersion existingBook = new CatalogItemVersion();
        existingBook.setBookId(request.getBookId());
        existingBook.setVersion(1);
        existingBook.setAuthor(request.getAuthor());
        existingBook.setGenre(BookGenre.TRAVEL);
        existingBook.setText(request.getText());
        existingBook.setTitle(request.getTitle());
        existingBook.setInactive(false);
        super.getTestDao().save(existingBook);

        // WHEN
        SubmitBookForPublishingResponse response = COMPONENT.provideSubmitBookForPublishingActivity().execute(request);

        // THEN
        // wait for queued status
        waitForExpectedStatus(response.getPublishingRecordId(),
            PublishingStatus.QUEUED);
        // wait for in progress status
        waitForExpectedStatus(response.getPublishingRecordId(),
            PublishingStatus.IN_PROGRESS);

        SubmitBookForPublishingHelper.waitForPublishing();

        // wait for successful status
        PublishingStatusRecord successful = waitForExpectedStatus(response.getPublishingRecordId(),
            PublishingStatus.SUCCESSFUL);

        assertNotNull(successful.getBookId(), "A successful PublishingStatusRecord should contain a bookId.");

        // a new book version should exist now
        GetBookRequest getBookRequest = GetBookRequest.builder()
            .withBookId(successful.getBookId())
            .build();

        GetBookResponse getBookResponse = COMPONENT.provideGetBookActivity().execute(getBookRequest);

        Book book = getBookResponse.getBook();
        assertEquals(book.getAuthor(), request.getAuthor(), "Expected a successful book publish request" +
            "to create a book with the provided author");
        assertEquals(book.getBookId(), successful.getBookId(), "Expected a successful book publish request" +
            "to create a book with the request's book ID");
        assertEquals(book.getGenre(), request.getGenre(), "Expected a successful book publish request" +
            "to create a book with the provided genre");
        assertEquals(book.getText(), request.getText(), "Expected a successful book publish request" +
            "to create a book with the provided text");
        assertEquals(book.getTitle(), request.getTitle(), "Expected a successful book publish request" +
            "to create a book with the provided title");
        assertEquals(book.getVersion(), existingBook.getVersion() + 1, "Expected a successful book publish request" +
            "to create a new book with an incremented version");

        // previous book version should be marked inactive
        CatalogItemVersion versionOneBook = super.getTestDao().load(existingBook);
        assertTrue(versionOneBook.isInactive(), "Expected a successful book publish request" +
            "to mark the previous version inactive");
    }

    private PublishingStatusRecord waitForExpectedStatus(String publishingRecordId,
                                                         String expectedStatus) {
        System.out.println(String.format("Looking for publish request with ID %s in %s status.",
            publishingRecordId,
            expectedStatus));
        System.out.println(String.format("Making %s attempts with %s ms in between attempts.",
            MAX_GET_EXPECTED_STATUS_ATTEMPTS,
            GET_EXPECTED_STATUS_BUFFER.toMillis()));

        GetPublishingStatusRequest request = GetPublishingStatusRequest.builder()
            .withPublishingRecordId(publishingRecordId)
            .build();

        int currentAttempt = 1;
        while (currentAttempt <= MAX_GET_EXPECTED_STATUS_ATTEMPTS) {
            System.out.println(String.format("Attempt [%s]: Calling GetPublishingStatus", currentAttempt));

            GetPublishingStatusResponse response = COMPONENT.provideGetPublishingStatusActivity().execute(request);

            Optional<PublishingStatusRecord> record = response.getPublishingStatusHistory().stream()
                .filter(p -> expectedStatus.equals(p.getStatus()))
                .findFirst();

            if (record.isPresent()) {
                System.out.println(String.format("Attempt [%s]: Found record! [%s]",
                    currentAttempt,
                    record.get()));

                return record.get();
            }

            System.out.println(String.format("Attempt [%s]: Did not find a record. " +
                    "Waiting %s milliseconds before next attempt",
                currentAttempt,
                GET_EXPECTED_STATUS_BUFFER.toMillis()));
            SubmitBookForPublishingHelper.wait(GET_EXPECTED_STATUS_BUFFER);

            currentAttempt++;
        }

        fail(String.format("Publishing status record with ID %s " +
                "did not reach the expected state %s" +
                "in %s attempts and %s milliseconds",
            publishingRecordId,
            expectedStatus,
            MAX_GET_EXPECTED_STATUS_ATTEMPTS,
            MAX_GET_EXPECTED_STATUS_ATTEMPTS * GET_EXPECTED_STATUS_BUFFER.toMillis()));
        return null;
    }
}
