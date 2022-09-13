package com.amazon.ata.kindlepublishingservice.helpers;

public class TctResult {
    boolean passed;
    String errorMessage;
    TctId tctId;

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TctId getTctId() {
        return tctId;
    }

    public void setTctId(TctId tctId) {
        this.tctId = tctId;
    }
}
