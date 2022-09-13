package com.amazon.ata.resources.debugging.dependencies;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A service that interacts with Amazon's product catalogue. Products can be looked up by their asin.
 */
public class AmazonProductService {

    private File catalogueFile;
    private ObjectMapper mapper;
    private Map<String, Product> asinMapping;

    /**
     * Creates an in memory Amazon catalog service. Reads product information from a file.
     * @param catalogueFile - lines of JSON, each representing a Product
     */
    public AmazonProductService(File catalogueFile) {
        this.catalogueFile = catalogueFile;
        this.mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        asinMapping = new HashMap<>();
        try {
            List<String> productDescriptions = FileUtils.readLines(catalogueFile, Charset.defaultCharset());

            for (String productDescription : productDescriptions){
                Product product = mapper.readValue(productDescription, Product.class);
                asinMapping.put(product.getAsin(), product);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to connect to the ProductService.", e);
        }
    }

    /**
     * Retrieve product information from Amazon's catalogue.
     * @param asin - the asin identifier for the product to be retrieved. The asin cannot be null/empty/whitespace.
     * @return a populated Product object if the asin corresponds to a catalogue item, null otherwise.
     */
    public Product getProductByAsin(String asin) {
        if(StringUtils.isBlank(asin)){
            throw new IllegalArgumentException("An asin must be provided.");
        }
        return asinMapping.getOrDefault(asin, null);
    }
}
