package com.amazon.ata.resources.debugging.dependencies;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AmazonProductServiceTest {

    private static final String TEST_FILE_PATH = "./tst/resources/catalog.json";

    private AmazonProductService serviceUnderTest;

    @BeforeEach
    public void setup(){
        serviceUnderTest = new AmazonProductService(new File(TEST_FILE_PATH));
    }

    @Test
    public void getProductByAsin_asinRecognized_productReturned(){
        // GIVEN
        // recognized asin
        String recognizedAsin = "B00006IEJB";

        // WHEN
        // getProductByAsin called with a recognized asin value
        Product product = serviceUnderTest.getProductByAsin(recognizedAsin);

        // THEN
        // product returned should be non null
        assertNotNull(product, "Expected a recognized asin to return non null product when getProductByAsin" +
            "is called.");
        // product returned should match the requested asin
        assertEquals(recognizedAsin, product.getAsin(), "Expected product returned by getProductByAsin to " +
            "have an asin matching the asin requested for.");
    }

    @Test
    public void getProductByAsin_asinUnrecognized_nullReturned(){
        // GIVEN
        // unrecognized asin
        String unrecognizedAsin = "B123456789";

        // WHEN
        // getProductByAsin called with an unrecognized asin value
        Product product = serviceUnderTest.getProductByAsin(unrecognizedAsin);

        // THEN
        // product returned should be null
        assertNull(product, "Expected an unrecognized asin to return null when getProductByAsin is called.");
    }

    @Test
    public void getProductByAsin_asinNull_exceptionRaised(){
        // GIVEN
        // null asin
        String nullAsin = null;

        // WHEN & THEN
        // getProductByAsin called with null asin value, IllegalArgumentException thrown
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            serviceUnderTest.getProductByAsin(nullAsin);
        });
    }

    @Test
    public void getProductByAsin_asinEmpty_exceptionRaised(){
        // GIVEN
        // empty asin
        String emptyAsin = "";

        // WHEN & THEN
        // getProductByAsin called with empty asin value, IllegalArgumentException thrown
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            serviceUnderTest.getProductByAsin(emptyAsin);
        });
    }

    @Test
    public void getProductByAsin_asinWhitespace_exceptionRaised(){
        // GIVEN
        // whitespace asin
        String whitespaceAsin = "  ";

        // WHEN & THEN
        // getProductByAsin called with whitespace asin value, IllegalArgumentException thrown
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            serviceUnderTest.getProductByAsin(whitespaceAsin);
        });
    }
}
