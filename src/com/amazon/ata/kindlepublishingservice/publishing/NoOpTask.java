package com.amazon.ata.kindlepublishingservice.publishing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * A Runnable that performs no action in its run method. It is currently being scheduled to run repeatedly. This class
 * comes with the service, but should be removed once your publishing task that implements Runnable is created and the
 * providePublishingTask() method is updated to return it in the PublishingModiule.
 */
public class NoOpTask implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(BookPublisher.class);

    /**
     * Constructs a NoOpTask object.
     */
    @Inject
    public NoOpTask() {}

    @Override
    public void run() {
        LOGGER.info("NoOp Task executed.");
    }
}
