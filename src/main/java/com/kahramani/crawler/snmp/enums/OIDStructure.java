package com.kahramani.crawler.snmp.enums;

/**
 * Created by kahramani on 11/24/2016.
 */
public enum OIDStructure {

    /**
     * oid
     */
    UPTIME(".1.3.6.1.2.1.1.3.0"),
    SW_HUAWEI_VLAN(".1.3.6.1.4.1.2011.5.25.42.2.1.32.1.12"),
    SW_CISCO_VLAN(".1.3.6.1.4.1.9.9.46.1.3.1.1.4"),
    OLT_HUAWEI_ONT_SERIAL_NUMBER(".1.3.6.1.4.1.2011.6.128.1.1.2.43.1.3"),
    OLT_NOKIA_ONT_SERIAL_NUMBER(".1.3.6.1.4.1.637.61.1.35.10.1.1.5");

    private String oid;

    OIDStructure(String oid) {
        this.oid = oid;
    }

    public String get() {
        return oid;
    }
}
