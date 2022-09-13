package com.amazon.ata.resources.maps.gpmreminder.model;

import java.util.List;

/**
 * Holds aggregated data about GPMs, DeviceParts, and their associations.
 */
public class DevicePartReport {

    private List<CountAssociation> gpmParts;
    private List<CountAssociation> partGpms;

    /**
     * Constructs a DevicePartReport, passing in lists of the number of devices each GPM owns, and the number of GPMs
     * responsible for each device.
     * @param gpmParts a list of GPMs associated with the number of devices they own.
     * @param partGpms a list of DeviceParts associated with the number of GPMs that own them.
     */
    public DevicePartReport(List<CountAssociation> gpmParts, List<CountAssociation> partGpms) {
        this.gpmParts = gpmParts;
        this.partGpms = partGpms;
    }

    public List<CountAssociation> getGpmParts() {
        return gpmParts;
    }

    public List<CountAssociation> getPartGpms() {
        return partGpms;
    }
}
