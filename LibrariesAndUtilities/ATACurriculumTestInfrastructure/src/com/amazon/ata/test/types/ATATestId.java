package com.amazon.ata.test.types;

/**
 * A simplified version of JUnit's TestIdentifier.
 */
public class ATATestId {
    /**
     * For our use case, the display name is often the name of the test method.
     * If the test case is a parameterized test, then this will be the toString() of the parameter value.
     */
    private String displayName;
    /**
     * For our use case, the parent display name is often the name of the class the test method is a part of.
     * If the test case is a parameterized test, then this will be the test name.
     */
    private String parentDisplayName;

    private ATATestId(String displayName, String parentDisplayName) {
        this.displayName = displayName;
        this.parentDisplayName = parentDisplayName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getParentDisplayName() {
        return parentDisplayName;
    }

    public void setParentDisplayName(String parentDisplayName) {
        this.parentDisplayName = parentDisplayName;
    }

    public static class Builder {
        private String displayName;
        private String parentDisplayName;

        public Builder withDisplayName(String displayNameToUse) {
            this.displayName = displayNameToUse;
            return this;
        }

        public Builder withParentDisplayName(String parentDisplayNameToUse) {
            this.parentDisplayName = parentDisplayNameToUse;
            return this;
        }

        public ATATestId build() {
            return new ATATestId(displayName, parentDisplayName);
        }
    }

}
