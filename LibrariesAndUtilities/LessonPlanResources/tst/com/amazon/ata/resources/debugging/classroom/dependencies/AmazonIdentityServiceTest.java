package com.amazon.ata.resources.debugging.dependencies;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AmazonIdentityServiceTest {

    private static final String TEST_FILE_PATH = "./tst/resources/customers.txt";

    private AmazonIdentityService serviceUnderTest;

    @BeforeEach
    public void setup() {
        serviceUnderTest = new AmazonIdentityService(new File(TEST_FILE_PATH));
    }

    @Test
    public void validateCustomer_customerIdRecognized_returnsTrue(){
        // GIVEN
        // recognized customerId
        String recognizedCustomerId = "amzn1.account.AEZI3A063427738YROOFT8WCXKDE";

        // WHEN
        // validateCustomer called with a recognized customer id
        boolean result = serviceUnderTest.validateCustomer(recognizedCustomerId);

        // THEN
        // result should be true, customer valid
        assertTrue(result, "Expected a true result when validating a known customerId");
    }

    @Test
    public void validateCustomer_customerIdUnrecognized_returnsFalse(){
        // GIVEN
        // unrecognized customerId
        String unrecognizedCustomerId = "amzn1.account.AE12121212121212121212121212";

        // WHEN
        // validateCustomer called with an unrecognized customer id
        boolean result = serviceUnderTest.validateCustomer(unrecognizedCustomerId);

        // THEN
        // result should be false, customer invalid
        assertFalse(result, "Expected a false result when validating a unknown customerId");
    }

    @Test
    public void validateCustomer_customerIdEmpty_exceptionRaised(){
        // GIVEN
        // empty customerId
        String emptyCustomerId = "";

        // WHEN & THEN
        // validateCustomer called with empty customerId, IllegalArgumentException thrown
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            serviceUnderTest.validateCustomer(emptyCustomerId);
        });
    }

    @Test
    public void validateCustomer_customerIdNull_exceptionRaised(){
        // GIVEN
        // null customerId
        String nullCustomerId = null;

        // WHEN & THEN
        // validateCustomer called with null customerId, IllegalArgumentException thrown
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            serviceUnderTest.validateCustomer(nullCustomerId);
        });
    }

    @Test
    public void validateCustomer_customerIdWhitespace_exceptionRaised(){
        // GIVEN
        // whitespace customerId
        String whitespaceCustomerId = "  ";

        // WHEN & THEN
        // validateCustomer called with whitespace customerId, IllegalArgumentException thrown
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            serviceUnderTest.validateCustomer(whitespaceCustomerId);
        });
    }
}
