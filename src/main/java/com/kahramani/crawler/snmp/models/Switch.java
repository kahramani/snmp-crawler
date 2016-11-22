package com.kahramani.crawler.snmp.models;

import java.util.Set;

/**
 * Created by kahramani on 11/22/2016.
 */
public class Switch extends NetworkElement {

    private Set<String> vlanSet;

    public Switch(String ipAddress, String hostName) {
        super(ipAddress, hostName);
    }

    public Set<String> getVlanSet() {
        return vlanSet;
    }

    public void setVlanSet(Set<String> vlanSet) {
        this.vlanSet = vlanSet;
    }

    public String toString() {
        return new StringBuilder()
                .append("ipAddress: ").append(getIpAddress())
                .append(", hostName: ").append(getHostName())
                .append(", deviceModel: ").append(getDeviceModel())
                .append(", isReachable: ").append(isReachable())
                .append(", vlanSet: ").append(getVlanSet()).toString();
    }
}
