package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;

public final class BookPublishTask implements Runnable{
    public  PublishingStatusDao publishingStatusDao;
    private BookPublishRequestManager bookPublishRequestManager;
    public CatalogDao catalogDao;


   @Inject
    public BookPublishTask(PublishingStatusDao publishingStatusDao, BookPublishRequestManager bookPublishRequestManager,
                           CatalogDao catalogDao) {
        this.publishingStatusDao = publishingStatusDao;
        this.bookPublishRequestManager = bookPublishRequestManager;
        this.catalogDao = catalogDao;
    }

    @Override
    public void run() {
    BookPublishRequest current = bookPublishRequestManager.getBookPublishRequestToProcess();
        System.out.println("run method called");
    if(current == null){
        return;
    }
    PublishingStatusItem publishingStatusItem = publishingStatusDao.setPublishingStatus(current.getPublishingRecordId(),
            PublishingRecordStatus.IN_PROGRESS, current.getBookId());

    //call format from KindleFormatConverter
        KindleFormattedBook kindleFormattedBook = KindleFormatConverter.format(current);
        try{

            //call createOrUpdateBook from CatalogueDAO with the return from KindleFormatConverter
            CatalogItemVersion catalogItemVersion = catalogDao.createOrUpdateBook(kindleFormattedBook);

            // call setPublishingStatus with successful status
            PublishingStatusItem publishingStatusItemSuccess = publishingStatusDao.setPublishingStatus(current.getPublishingRecordId(),
                    PublishingRecordStatus.SUCCESSFUL, catalogItemVersion.getBookId());

        } catch(BookNotFoundException e){

            // call setPublishingStatus with failed status;
            PublishingStatusItem publishingStatusItemSuccess = publishingStatusDao.setPublishingStatus(current.getPublishingRecordId(),
                    PublishingRecordStatus.FAILED, current.getBookId(), "Publishing has failed");
        }
    }
}
