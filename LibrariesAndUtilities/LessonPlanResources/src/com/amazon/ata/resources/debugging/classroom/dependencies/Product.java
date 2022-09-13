package com.amazon.ata.resources.debugging.dependencies;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * An Amazon retail product. Includes the name of the product shown on the website (title), it's asin identifier,
 * the vendor that is offering the product at the present listPrice, and whether an item is eligible for Subscribe and
 * Save (isSNS).
 */
public class Product {
    private String title;
    private String asin;
    private String vendor;
    @JsonProperty(value="isSNS")
    private boolean isSNS;
    private BigDecimal listPrice;

    public Product(){}

    public Product(String title, String asin, String vendor, boolean isSns, BigDecimal listPrice){
        this.title = title;
        this.asin = asin;
        this.vendor = vendor;
        this.isSNS = isSns;
        this.listPrice = listPrice;
    }

    public String getTitle() {
        return title;
    }

    public String getAsin() {
        return asin;
    }

    public String getVendor() {
        return vendor;
    }

    public boolean isSNS() {
        return isSNS;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }
}
