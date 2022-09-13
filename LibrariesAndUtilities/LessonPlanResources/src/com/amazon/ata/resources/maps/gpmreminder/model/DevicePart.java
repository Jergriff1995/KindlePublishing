package com.amazon.ata.resources.maps.gpmreminder.model;

/**
 * Represents a single DevicePart used in building a device. In the AgilePLM system, this includes an ASIN and
 * some metadata. In this example, it's the ASIN and the Responsible GPM's ID.
 */
public class DevicePart {
    private final String asin;
    private final String responsibleGpmId;

    /**
     * Construct a DevicePart given its ASIN and the GPM who manages it.
     * @param asin The ASIN of the DevicePart.
     * @param responsibleGpmId The ID of the GPM responsible for this DevicePart.
     */
    public DevicePart(final String asin, final String responsibleGpmId) {
        this.asin = asin;
        this.responsibleGpmId = responsibleGpmId;
    }

    public String getAsin() {
        return asin;
    }

    public String getResponsibleGpmId() {
        return responsibleGpmId;
    }
}
