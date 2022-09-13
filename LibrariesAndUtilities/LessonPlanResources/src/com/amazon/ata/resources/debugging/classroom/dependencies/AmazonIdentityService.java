package com.amazon.ata.resources.debugging.dependencies;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * A service that interacts with Amazon's identity listings. This service can be used to validate a customerId.
 */
public class AmazonIdentityService {

    private File customerIdFile;
    private List<String> customerIdentities;

    /**
     * Creates an in memory service to validate Amazon customer identities. Customers that exist within the provided
     * file will be validated.
     * @param customerIdFile - a file with one customerId per line
     * @throws IllegalStateException if the file cannot be read and the service "started"
     */
    public AmazonIdentityService(File customerIdFile) {
        this.customerIdFile = customerIdFile;
        try {
            List<String> productDescriptions = FileUtils.readLines(customerIdFile, Charset.defaultCharset());
            customerIdentities = new ArrayList<>(productDescriptions);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to connect to the AmazonIdentityService.", e);
        }
    }

    /**
     * Returns true if the provided customerId corresponds to a valid Amazon account.
     * @param customerId - the id of the customer to validate. The customerId cannot be null/empty/whitespace.
     * @return true if the customerId exists, false otherwise
     */
    public boolean validateCustomer(String customerId){
        if(StringUtils.isBlank(customerId)){
            throw new IllegalArgumentException("A customerId must be provided.");
        }
        return customerIdentities.contains(customerId);
    }
}
