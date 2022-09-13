package com.amazon.ata.kindlepublishingservice.publishing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class BookPublisherTest {

    @Mock
    private ScheduledExecutorService scheduledExecutorService;

    @Mock
    private Runnable publishTask;

    @InjectMocks
    private BookPublisher bookPublisher;

    @BeforeEach
    public void setup(){
        initMocks(this);
    }

    @Test
    public void start_notStarted_scheduleStarted() {
        // GIVEN

        // WHEN
        bookPublisher.start();

        // THEN
        verify(scheduledExecutorService).scheduleWithFixedDelay(publishTask, 0, 1,
            TimeUnit.SECONDS);
        assertTrue(bookPublisher.isRunning(), "Expected publisher to be running after a call to start().");
    }

    @Test
    public void start_alreadyStarted_scheduleNotRestarted() {
        // GIVEN
        bookPublisher.start();

        // WHEN
        bookPublisher.start();

        // THEN
        // schedule started in the call to start in the GIVEN, and not again from the call to start in the WHEN
        verify(scheduledExecutorService, times(1)).scheduleWithFixedDelay(publishTask, 0, 1,
            TimeUnit.SECONDS);
        assertTrue(bookPublisher.isRunning(), "Expected publisher to be running after a call to start().");
    }

    @Test
    public void start_afterStop_scheduleRestarted() {
        // GIVEN
        bookPublisher.start();
        bookPublisher.stop();

        // WHEN
        bookPublisher.start();

        // THEN
        // schedule started in the call to start in the GIVEN, schedule stopped in the call to stop in the GIVEN,
        // schedule restarted in the call to start in the WHEN
        verify(scheduledExecutorService, times(2)).scheduleWithFixedDelay(publishTask, 0, 1,
            TimeUnit.SECONDS);
        assertTrue(bookPublisher.isRunning(), "Expected publisher to be running after a call to start().");
    }

    @Test
    public void isStarted_notStartCall_returnsFalse() {
        // GIVEN

        // WHEN
        boolean isRunning = bookPublisher.isRunning();

        // THEN
        assertFalse(isRunning, "Expected publisher to be created in the not running state.");
    }

    @Test
    public void stop_publisherStarted_shutsDown() {
        // GIVEN
        bookPublisher.start();

        // WHEN
        bookPublisher.stop();

        // THEN
        verify(scheduledExecutorService).shutdown();
        assertFalse(bookPublisher.isRunning(), "Expected publisher to be shutdown after a call to stop().");
    }

    @Test
    public void stop_publisherNotStarted_shutsDown() {
        // GIVEN

        // WHEN
        bookPublisher.stop();

        // THEN
        verify(scheduledExecutorService).shutdown();
        assertFalse(bookPublisher.isRunning(), "Expected publisher to be shutdown after a call to stop().");
    }
}
