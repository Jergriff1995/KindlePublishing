package com.amazon.ata.kindlepublishingservice.publishing;

import org.junit.jupiter.api.Test;

public class NoOpTaskTest {

    @Test
    public void run_completesSuccessfully() {
        // GIVEN
        NoOpTask task = new NoOpTask();

        // WHEN && THEN
        task.run();
    }
}
