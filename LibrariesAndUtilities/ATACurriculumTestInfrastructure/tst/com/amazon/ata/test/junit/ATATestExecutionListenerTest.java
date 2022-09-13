package com.amazon.ata.test.junit;

import com.amazon.ata.test.types.ATATestResult;
import com.amazon.ata.test.types.ATATestSuiteReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestIdentifier;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ATATestExecutionListenerTest {
    private static final TestIdentifier TEST_IDENTIFIER = TestIdentifier.from(
            new FakeTestDescriptor("1", TestDescriptor.Type.TEST));
    private static final TestIdentifier CONTAINER_IDENTIFIER = TestIdentifier.from(
            new FakeTestDescriptor("2", TestDescriptor.Type.CONTAINER));

    @Captor
    private ArgumentCaptor<ATATestResult> ataTestResultCaptor;

    @Mock
    private ATATestSuiteReport ataTestSuiteReport;

    @Mock
    private TestExecutionResult testExecutionResult;

    @InjectMocks
    private ATATestExecutionListener listener;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void executionFinished_container_testRunAdded() {
        // GIVEN
        when(testExecutionResult.getStatus()).thenReturn(TestExecutionResult.Status.SUCCESSFUL);
        Throwable throwable = new IllegalArgumentException();
        when(testExecutionResult.getThrowable()).thenReturn(Optional.of(throwable));

        // WHEN
        listener.executionFinished(CONTAINER_IDENTIFIER, testExecutionResult);

        // THEN
        verify(ataTestSuiteReport).addTestRun(ataTestResultCaptor.capture());
        ATATestResult ataTestResult = ataTestResultCaptor.getValue();
        assertEquals("2", ataTestResult.getTestId().getDisplayName());
        assertTrue(ataTestResult.isPassed());
        assertEquals(throwable.toString(), ataTestResult.getErrorMessage().get());
    }

    @Test
    public void executionFinished_testRun_testRunAdded() {
        // GIVEN
        when(testExecutionResult.getStatus()).thenReturn(TestExecutionResult.Status.SUCCESSFUL);
        Throwable throwable = new IllegalArgumentException();
        when(testExecutionResult.getThrowable()).thenReturn(Optional.of(throwable));

        // WHEN
        listener.executionFinished(TEST_IDENTIFIER, testExecutionResult);

        // THEN
        verify(ataTestSuiteReport).addTestRun(ataTestResultCaptor.capture());
        ATATestResult ataTestResult = ataTestResultCaptor.getValue();
        assertEquals("1", ataTestResult.getTestId().getDisplayName());
        assertTrue(ataTestResult.isPassed());
        assertEquals(throwable.toString(), ataTestResult.getErrorMessage().get());
    }
}
