package com.kahramani.crawler.snmp.models;

import com.kahramani.crawler.snmp.enums.DeviceModel;
import com.kahramani.crawler.snmp.utils.SnmpUtils;

/**
 * Created by kahramani on 11/22/2016.
 */
public class NetworkElement {

    private String ipAddress;
    private String hostName;
    private DeviceModel deviceModel;
    private boolean isReachable;

    public NetworkElement(String ipAddress, String hostName) {
        this.ipAddress = ipAddress;
        this.hostName = hostName;
        this.deviceModel = SnmpUtils.getDeviceModelByHostName(hostName);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public DeviceModel getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }

    public boolean isReachable() {
        return isReachable;
    }

    public void setIsReachable(boolean isReachable) {
        this.isReachable = isReachable;
    }

    public String toString() {
        return new StringBuilder()
                .append("ipAddress: ").append(getIpAddress())
                .append(", hostName: ").append(getHostName())
                .append(", deviceModel: " + getDeviceModel())
                .append(", isReachable: " + isReachable()).toString();
    }
}
