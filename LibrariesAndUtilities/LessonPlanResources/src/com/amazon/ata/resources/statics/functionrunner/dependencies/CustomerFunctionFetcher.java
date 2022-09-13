package com.amazon.ata.resources.statics.functionrunner.dependencies;

/**
 * Responsible for returning {@code CustomerFunction} objects based on queries
 * for them (for now, supports fetching by function ID only).
 */
public class CustomerFunctionFetcher {
    /**
     * Given a functionId of a function to run, fetches the corresponding {@code CustomerFunction}
     * to run.
     * @param functionId The function identifier for the {@code CustomerFunction} to retrieve
     * @return the {@code CustomerFunction} corresponding to the function ID if found, null otherwise
     */
    public CustomerFunction fetchFunction(String functionId) {
        return new TestCustomerFunction();
    }
}
