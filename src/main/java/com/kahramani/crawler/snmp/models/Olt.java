package com.kahramani.crawler.snmp.models;

/**
 * Created by kahramani on 11/22/2016.
 */
public class Olt extends NetworkElement {

    public Olt(String ipAddress, String hostName) {
        super(ipAddress, hostName);
    }

    public String toString() {
        return new StringBuilder()
                .append("ipAddress: ").append(getIpAddress())
                .append(", hostName: ").append(getHostName())
                .append(", deviceModel: " + getDeviceModel())
                .append(", isReachable: " + isReachable()).toString();
    }
}
