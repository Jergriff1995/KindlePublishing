package com.amazon.ata.test.types;

import java.util.Optional;

/**
 * The result of running the test that corresponds to a {@code ATATestId}.
 */
public class ATATestResult {
    private ATATestId testId;
    private boolean passed;
    /**
     * An error message will only exist if the test has not passed.
     */
    private Optional<String> errorMessage;

    private ATATestResult(ATATestId testId, boolean passed, Optional<String> errorMessage) {
        this.testId = testId;
        this.passed = passed;
        this.errorMessage = errorMessage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ATATestId getTestId() {
        return testId;
    }

    public void setTestId(ATATestId testId) {
        this.testId = testId;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Optional<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Optional<String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static class Builder {
        private ATATestId testId;
        private boolean passed;
        private Optional<String> errorMessage = Optional.empty();

        public Builder withTestId(ATATestId testIdToUse) {
            this.testId = testIdToUse;
            return this;
        }

        public Builder withPassed(boolean passedToUse) {
            this.passed = passedToUse;
            return this;
        }

        public Builder withErrorMessage(String errorMessageToUse) {
            this.errorMessage = Optional.ofNullable(errorMessageToUse);
            return this;
        }

        public Builder withErrorMessage(Optional<String> errorMessageToUse) {
            this.errorMessage = errorMessageToUse;
            return this;
        }

        public ATATestResult build() {
            return new ATATestResult(testId, passed, errorMessage);
        }
    }
}
