package com.amazon.ata.resources.lists.prework;

/**
 * Represents a destination that's part of an itinerary. Contains information on which location to
 * visit and how many days to spend at the location.
 */
public class Destination {
    private final String location;
    private final int daysAtLocation;

    /**
     * Constructs new destination
     * @param location Defines location to visit e.g. Paris or London.
     * @param daysAtLocation Defines how many days to spend at the selected location.
     */
    public Destination(String location, int daysAtLocation) {
        this.location = location;
        this.daysAtLocation = daysAtLocation;
    }

    /**
     * Get location which is going to be visited
     * @return location name
     */
    public String getLocation() {
        return location;
    }

    /**
     * Get number of days to spend at the location
     * @return number of days to spend at location
     */
    public int getDaysAtLocation() {
        return daysAtLocation;
    }
}