package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;

import javax.inject.Inject;

public class GetPublishingStatusActivity {
    private PublishingStatusDao publishingStatusDao;
    private CatalogDao catalogDao;

    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao, CatalogDao catalogDao) {
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {
//        if(publishingStatusRequest.getPublishingRecordId() != null ){
//            throw new PublishingStatusNotFoundException("Book ID must be provided.");
//        }
        GetPublishingStatusResponse getPublishingStatusResponse =
                new GetPublishingStatusResponse(publishingStatusDao.getListOfStatus(publishingStatusRequest.getPublishingRecordId()));
        return getPublishingStatusResponse;
    }
}
