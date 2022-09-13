package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.Queue;

@Singleton
public final class BookPublishRequestManager {
    private Queue<BookPublishRequest> bookPublishingQueue;

    @Inject
    public BookPublishRequestManager() {
        bookPublishingQueue = new LinkedList<>();
    }

    public void addBookPublishRequest(BookPublishRequest providedBook){
        bookPublishingQueue.add(providedBook);
    }

    public BookPublishRequest getBookPublishRequestToProcess(){
        BookPublishRequest nextBookPublishingRequest = bookPublishingQueue.poll();
        return nextBookPublishingRequest;
    }
}
