package com.kahramani.crawler.snmp.models;

/**
 * Created by kahramani on 11/22/2016.
 */
public class SwitchPortData {

    private Switch sw;
    private String vlan;
    private String port;
    private String macAddress;

    public SwitchPortData(Switch sw) {
        this.sw = sw;
    }

    public Switch getSw() {
        return sw;
    }

    public void setSw(Switch sw) {
        this.sw = sw;
    }

    public String getVlan() {
        return vlan;
    }

    public void setVlan(String vlan) {
        this.vlan = vlan;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String toString() {
        return new StringBuilder()
                .append("sw: ").append(getSw())
                .append(", vlan: ").append(getVlan())
                .append(", port: ").append(getPort())
                .append(", macAddress: ").append(getMacAddress()).toString();
    }
}
