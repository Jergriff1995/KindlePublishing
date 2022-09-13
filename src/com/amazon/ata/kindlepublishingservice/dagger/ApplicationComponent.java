package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.activity.GetBookActivity;
import com.amazon.ata.kindlepublishingservice.activity.GetPublishingStatusActivity;
import com.amazon.ata.kindlepublishingservice.activity.RemoveBookFromCatalogActivity;
import com.amazon.ata.kindlepublishingservice.activity.SubmitBookForPublishingActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        ClientsModule.class,
        DataAccessModule.class,
        PublishingModule.class
})
public interface ApplicationComponent {
    GetBookActivity provideGetBookActivity();

    GetPublishingStatusActivity provideGetPublishingStatusActivity();

    RemoveBookFromCatalogActivity provideRemoveBookFromCatalogActivity();

    SubmitBookForPublishingActivity provideSubmitBookForPublishingActivity();

    ATAKindlePublishingServiceManager provideATAKindlePublishingServiceManager();
}
