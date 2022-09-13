package com.amazon.ata.kindlepublishingservice.helpers;

public class ExecuteTctSuiteRequest {

    String tctSuiteId;

    public ExecuteTctSuiteRequest(String tctSuiteId) {
        this.tctSuiteId = tctSuiteId;
    }

    public String getTctSuiteId() {
        return tctSuiteId;
    }

    public void setTctSuiteId(String tctSuiteId) {
        this.tctSuiteId = tctSuiteId;
    }
}
