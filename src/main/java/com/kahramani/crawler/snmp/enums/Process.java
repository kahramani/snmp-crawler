package com.kahramani.crawler.snmp.enums;

/**
 * Created by kahramani on 11/22/2016.
 */
public enum Process {

    SWITCH_SNMP("Snmp Switch"),
    OLT_SNMP("Snmp Olt");

    private final String name;

    Process(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
