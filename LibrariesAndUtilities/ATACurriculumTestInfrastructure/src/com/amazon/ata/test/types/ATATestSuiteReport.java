package com.amazon.ata.test.types;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The result of running each test within a suite of tests.
 */
public class ATATestSuiteReport {
    private ATATestSuiteId testSuiteId;
    private Collection<ATATestResult> results;

    public ATATestSuiteReport(ATATestSuiteId testSuiteId) {
        this(testSuiteId, new ArrayList<>());
    }

    public ATATestSuiteReport(ATATestSuiteId testSuiteId, Collection<ATATestResult> results) {
        this.testSuiteId = testSuiteId;
        this.results = results;
    }

    public ATATestSuiteId getTestSuiteId() {
        return testSuiteId;
    }

    public void setTestSuiteId(ATATestSuiteId testSuiteId) {
        this.testSuiteId = testSuiteId;
    }

    public Collection<ATATestResult> getResults() {
        return results;
    }

    public void setResults(Collection<ATATestResult> results) {
        this.results = results;
    }

    public void addTestRun(ATATestResult result) {
        results.add(result);
    }
}
