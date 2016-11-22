package com.kahramani.crawler.snmp.enums;

/**
 * Created by kahramani on 11/22/2016.
 */
public enum PropertyPrefix {
    SW_PREFIX("snmp.sw"),
    OLT_PREFIX("snmp.olt"),
    SOURCE_DB_PREFIX("db.source"),
    APPLICATION_DB_PREFIX("db.application");

    private String prefix;

    PropertyPrefix(String prefix) { this.prefix = prefix; }

    public String get() {return this.prefix; }

    public String toString() {
        return this.prefix;
    }
}
