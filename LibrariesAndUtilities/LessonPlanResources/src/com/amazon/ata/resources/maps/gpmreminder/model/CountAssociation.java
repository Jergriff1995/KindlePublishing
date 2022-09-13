package com.amazon.ata.resources.maps.gpmreminder.model;

/**
 * General association between some ID and a long count.
 *
 * In its original example, it was used to:
 *   1. Associate a GPM with the number of devices they manage.
 *   2. Associate a part with the number of GPMs responsible for it.
 */
public class CountAssociation {
    private String id;
    private long count;

    /**
     * Creates an association between the provided ID and the provided count.
     * @param id A unique identifier to associate with this count.
     * @param count The count associated with this unique identifier.
     */
    public CountAssociation(String id, long count) {
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public long getCount() {
        return count;
    }
}
