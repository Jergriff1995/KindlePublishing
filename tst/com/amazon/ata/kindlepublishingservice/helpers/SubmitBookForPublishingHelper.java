package com.amazon.ata.kindlepublishingservice.helpers;

import java.time.Duration;

import static org.testng.Assert.fail;

/**
 * Helper methods useful for working with the SubmitBookForPublishing operation.
 */
public class SubmitBookForPublishingHelper {
    // we process 1 book per second, with a 10s delay in "processing", plus some buffer
    // https://tiny.amazon.com/diptizts/codeamazpackATACblob2b94src
    // https://tiny.amazon.com/uhippi01/codeamazpackATACblob29ebsrc
    private static final Duration MAX_PUBLISHING_WAIT_TIME = Duration.ofSeconds(15L);

    /**
     * Wait for the preconfigured MAX_PUBLISHING_WAIT_TIME.
     */
    public static void waitForPublishing() {
        System.out.println("Waiting " + MAX_PUBLISHING_WAIT_TIME.getSeconds() + " seconds for publishing");
        wait(MAX_PUBLISHING_WAIT_TIME);
    }

    /**
     * Sleep for a variable {@link Duration}, fail the test on any
     * exceptions while sleeping.
     *
     * @param duration the duration to sleep for
     */
    public static void wait(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (Exception e) {
            fail("Caught exception while waiting for the publishing task! " +
                "Please create a CQA with the stack trace", e);
        }
    }

}
