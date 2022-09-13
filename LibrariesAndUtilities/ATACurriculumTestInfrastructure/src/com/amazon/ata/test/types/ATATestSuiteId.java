package com.amazon.ata.test.types;

/**
 * The identifier for the suite of tests to run. This often corresponds to a tag for a task in a project. The identifier
 * can be used to look up which test classes to run, where each test corresponds to an {@code ATATestId}.
 */
public class ATATestSuiteId {
    /**
     * The identifier for the full suite of tests. This is often the identifiers for our project tasks such as "PPT01".
     */
    private String testSuiteId;

    public ATATestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }

    public String getTestSuiteId() {
        return testSuiteId;
    }

    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }
}
