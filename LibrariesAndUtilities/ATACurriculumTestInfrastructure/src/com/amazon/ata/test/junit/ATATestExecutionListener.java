package com.amazon.ata.test.junit;

import com.amazon.ata.test.types.ATATestId;
import com.amazon.ata.test.types.ATATestResult;
import com.amazon.ata.test.types.ATATestSuiteReport;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

/**
 * Custom TestExecutionListener so we can populate a ATATestSuiteReport. After each test is executed
 * its results are appended to the ATATestSuiteReport.
 */
public class ATATestExecutionListener implements TestExecutionListener {
    private final ATATestSuiteReport suiteReport;

    /**
     * Instantiates a new ATATestExecutionListener.
     * @param suiteReport An empty report with the id of the test suite this object is listening to
     */
    public ATATestExecutionListener(ATATestSuiteReport suiteReport) {
        if (!suiteReport.getResults().isEmpty()) {
            throw new IllegalArgumentException("ATATestExecutionListener should be instantiated for a new suite" +
                    " of tests that haven't started executing.");
        }
        this.suiteReport = suiteReport;
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        ATATestId.Builder testIdBuilder = ATATestId.builder()
                .withDisplayName(testIdentifier.getDisplayName());
        testIdentifier.getParentId().ifPresent(testIdBuilder::withParentDisplayName);

        ATATestResult testResult = ATATestResult.builder()
                .withTestId(testIdBuilder.build())
                .withPassed(testExecutionResult.getStatus() == TestExecutionResult.Status.SUCCESSFUL)
                .withErrorMessage(testExecutionResult.getThrowable().map(Throwable::toString))
                .build();

        suiteReport.addTestRun(testResult);
    }

    public ATATestSuiteReport getResult() {
        return suiteReport;
    }
}
