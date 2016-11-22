package com.kahramani.crawler.snmp.enums;

/**
 * Created by kahramani on 11/22/2016.
 */
public enum DeviceModel {

    /**
     * device, vendor, host_name_prefix
     */
    UNIDENTIFIED    ("UNIDENTIFIED", "UNIDENTIFIED", "UNIDENTIFIED"),
    SW_HUAWEI       ("SWITCH", "HUAWEI", "SW_HUA"),
    SW_CISCO        ("SWITCH", "CISCO", "SW_CIS"),
    OLT_HUAWEI      ("OLT", "HUAWEI", "OLT_HUA"),
    OLT_NOKIA       ("OLT", "NOKIA", "SW_NOK");

    private final String device;
    private final String vendor;
    private final String prefix;

    DeviceModel(String device, String vendor, String prefix) {
        this.device = device;
        this.vendor = vendor;
        this.prefix = prefix;
    }

    public String getDevice() {
        return device;
    }

    public String getVendor() {
        return vendor;
    }

    public String getPrefix() {
        return prefix;
    }
}
