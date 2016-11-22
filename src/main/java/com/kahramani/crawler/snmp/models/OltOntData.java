package com.kahramani.crawler.snmp.models;

/**
 * Created by kahramani on 11/22/2016.
 */
public class OltOntData {

    private Olt olt;
    private String slot;
    private String port;
    private String ontNo;
    private String serialNumber;

    public Olt getOlt() {
        return olt;
    }

    public void setOlt(Olt olt) {
        this.olt = olt;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getOntNo() {
        return ontNo;
    }

    public void setOntNo(String ontNo) {
        this.ontNo = ontNo;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String toString() {
        return new StringBuilder()
                .append("olt: ").append(getOlt())
                .append(", slot: ").append(getSlot())
                .append(", port: ").append(getPort())
                .append(", ontNo: ").append(getOntNo())
                .append(", ontSerialNumber: ").append(getSerialNumber()).toString();
    }
}
