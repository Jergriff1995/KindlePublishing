package com.amazon.ata.test.junit;

import com.amazon.ata.test.types.ATATestResult;
import com.amazon.ata.test.types.ATATestSuiteId;
import com.amazon.ata.test.types.ATATestSuiteReport;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ATATestSuiteRunnerTest {

    @Test
    public void execute_passingTestSuite_resultIsPassed() {
        // GIVEN
        ATATestSuiteId testSuiteId = new ATATestSuiteId("PASSED");

        // WHEN
        ATATestSuiteReport suiteReport = ATATestSuiteRunner.execute(testSuiteId);

        // THEN
        assertEquals(testSuiteId, suiteReport.getTestSuiteId(),
                "Expected result to be be for provided testSuiteId");
        assertEquals(4, suiteReport.getResults().size());
        for (ATATestResult testResult : suiteReport.getResults()) {
            assertTrue(testResult.isPassed());
        }
    }

    @Test
    public void execute_failingTestSuite_resultIsFailed() {
        // GIVEN
        ATATestSuiteId testSuiteId = new ATATestSuiteId("FAILED");

        // WHEN
        ATATestSuiteReport suiteReport = ATATestSuiteRunner.execute(testSuiteId);

        // THEN
        assertEquals(testSuiteId, suiteReport.getTestSuiteId(),
                "Expected result to be be for provided testSuiteId");
        assertEquals(4, suiteReport.getResults().size());

        int failedCount = 0;
        for (ATATestResult testResult : suiteReport.getResults()) {
            if (!testResult.isPassed()) {
                failedCount++;
            }
        }

        assertEquals(2, failedCount, "Expected 2 test failures!");
    }

    @Test
    public void execute_failingTestSetup_resultIsFailed() {
        // GIVEN
        ATATestSuiteId testSuiteId = new ATATestSuiteId("SETUP_FAILS");

        // WHEN
        ATATestSuiteReport suiteReport = ATATestSuiteRunner.execute(testSuiteId);

        // THEN
        assertEquals(testSuiteId, suiteReport.getTestSuiteId(),
            "Expected result to be be for provided testSuiteId");
        assertEquals(2, suiteReport.getResults().size());

        int failedCount = 0;
        for (ATATestResult testResult : suiteReport.getResults()) {
            if (!testResult.isPassed()) {
                failedCount++;
            }
        }

        assertEquals(1, failedCount, "Expected 1 test failure!");
    }

    @Test
    public void execute_mixedResultsTestSuite_resultIsFailed() {
        // GIVEN
        ATATestSuiteId testSuiteId = new ATATestSuiteId("MIXED");

        // WHEN
        ATATestSuiteReport suiteReport = ATATestSuiteRunner.execute(testSuiteId);

        // THEN
        assertEquals(testSuiteId, suiteReport.getTestSuiteId(),
                "Expected result to be be for provided testSuiteId");
        assertEquals(4, suiteReport.getResults().size());
        int countPassed = 0;
        int countFailed = 0;
        for (ATATestResult testResult : suiteReport.getResults()) {
            if (testResult.isPassed()) {
                countPassed++;
            } else {
                countFailed++;
            }
        }
        assertEquals(3, countPassed);
        assertEquals(1, countFailed);
    }

    @Test
    public void execute_exceptionTestSuite_resultHasErrorMessage() {
        // GIVEN
        ATATestSuiteId testSuiteId = new ATATestSuiteId("EXCEPTION");

        // WHEN
        ATATestSuiteReport suiteReport = ATATestSuiteRunner.execute(testSuiteId);

        // THEN
        assertEquals(testSuiteId, suiteReport.getTestSuiteId(),
            "Expected result to be be for provided testSuiteId");
        assertEquals(3, suiteReport.getResults().size());

        int errorCount = 0;
        for (ATATestResult testResult : suiteReport.getResults()) {
            if (testResult.getErrorMessage().isPresent()) {
                errorCount ++;
                assertEquals("java.lang.RuntimeException: message", testResult.getErrorMessage().get());
            }
        }
        assertEquals(1, errorCount, "Expected to only have 1 error!");
    }

}
