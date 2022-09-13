package com.amazon.ata.resources.maps.gpmreminder.client;

import com.amazon.ata.resources.maps.gpmreminder.model.Amazonian;
import com.amazon.ata.resources.maps.gpmreminder.model.DevicePart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlmClient {
    private List<Amazonian> gpms;
    private List<DevicePart> deviceParts;

    private static final List<Amazonian> TEST_GPMS = Arrays.asList(
        new Amazonian("judeanth", "judeanth@amazon.com"),
        new Amazonian("beshoy", "beshoy@amazon.com"),
        new Amazonian("baxi", "baxi@amazon.com"),
        new Amazonian("khandoker", "khandoker@amazon.com"),
        new Amazonian("sirjames", "sirjames@amazon.com"),
        new Amazonian("jeff", "jeff@amazon.com"),
        new Amazonian("stevean", "stevean@amazon.com"),
        new Amazonian("ahoke", "ahoke@amazon.com"),
        new Amazonian("amit", "amit@amazon.com"),
        new Amazonian("mrbiggs", "mrbiggs@amazon.com"),
        new Amazonian("lijuan", "lijuan@amazon.com")
    );

    private static final List<DevicePart> TEST_PARTS = Arrays.asList(
        new DevicePart("B0096IQZ8S", "baxi"),
        new DevicePart("B016W6KHLC", "beshoy"),
        new DevicePart("B00CMDWUIS", "judeanth"),
        new DevicePart("B00TZKXTJ0", "khandoker"),
        new DevicePart("B01MQDJX6J", "baxi"),
        new DevicePart("B00BB7YETE", "judeanth"),
        new DevicePart("B00UW0MOCA", "beshoy"),
        new DevicePart("B00WM8DUKE", "beshoy"),
        new DevicePart("B006BSBRWK", "beshoy"),
        new DevicePart("B003L7VQMA", "sirjames"),
        new DevicePart("B002ID153S", "judeanth"),
        new DevicePart("B0000IGGIE", "stevean"),
        new DevicePart("B00LE6LBF7", "lijuan"),
        new DevicePart("B00Z0E16YX", "ahoke"),
        new DevicePart("B00QR1USAS", "lijuan"),
        new DevicePart("B0005LQRLR", "amit"),
        new DevicePart("B00380B7NT", "lijuan"),
        new DevicePart("B00QR1USAS", "lijuan"),
        new DevicePart("B00WM8DUKE", "amit"),
        new DevicePart("B00HF8J0H3", "judeanth"),
        new DevicePart("B00DVEL7H6", "khandoker"),
        new DevicePart("B00P17ZS3W", "baxi"),
        new DevicePart("B00QLN6YVH", "mrbiggs"),
        new DevicePart(null, "NOASIN"),
        new DevicePart("NOGPM", null)
    );

    public PlmClient() {
        this(TEST_GPMS, TEST_PARTS);
    }

    public PlmClient(List<Amazonian> gpms, List<DevicePart> deviceParts) {
        this.gpms = new ArrayList<>(gpms);
        this.deviceParts = new ArrayList<>(deviceParts);
    }

    /**
     * Find all the GPMs.
     *
     * @return The actual list of gpms internally used by the PLM system.
     */
    public List<Amazonian> getGpms() {
        System.out.println("Retrieving GPMs from remote system...");
        try {
            Thread.sleep(10L * gpms.size());
        } catch (InterruptedException e) {
            System.out.println("...Retrieved GPMs.");
        }
        return new ArrayList<>(this.gpms);
    }

    /**
     * Find all the DeviceParts needed to manufacture all devices.
     */
    public List<DevicePart> getDeviceParts() {
        System.out.println("Retrieving DeviceParts from remote system...");
        try {
            Thread.sleep(10L * deviceParts.size());
        } catch (InterruptedException e) {
            System.out.println("...Retrieved DeviceParts.");
        }
        return new ArrayList<>(deviceParts);
    }

}
