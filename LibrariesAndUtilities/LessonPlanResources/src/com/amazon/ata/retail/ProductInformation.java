package com.amazon.ata.retail;

import com.google.common.annotations.VisibleForTesting;

import java.util.Set;

public class ProductInformation {
    /**
     * An Amazon specific unique identifier for the product.
     */
    private String asin;

    /**
     * The display name to use when the product appears in search, detail page, etc.
     */
    private String displayName;

    /**
     * A description of the product that can be used if customers want to learn more information.
     */
    private String description;

    /**
     * URLs to images in S3.
     */
    private Set<String> imageUrls;

    public ProductInformation(String asin, String displayName, String description, Set<String> imageUrls) {
        this.asin = asin;
        this.displayName = displayName;
        this.description = description;
        this.imageUrls = imageUrls;
    }

    @VisibleForTesting
    public ProductInformation() {
    }

    public String getAsin() {
        return asin;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public double calculateSimilarity(ProductInformation other) {
        // This is just a dummy implementation, we would probably use machine learning here to
        // actually identify similar products.
        return Math.random();
    }
}
