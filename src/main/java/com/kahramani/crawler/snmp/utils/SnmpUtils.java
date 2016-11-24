package com.kahramani.crawler.snmp.utils;

import com.kahramani.crawler.snmp.enums.DeviceModel;

import org.springframework.util.Assert;

import java.util.Locale;

/**
 * Created by kahramani on 11/22/2016.
 */
public class SnmpUtils {

    public static final int DEFAULT_SNMP_PORT = 161;

    /**
     * to detect model of the device
     * @param hostName hostName of device
     * @return a DeviceModel enum
     */
    // TODO update here for your purposes
    public static DeviceModel getDeviceModelByHostName(String hostName) {
        Assert.hasText(hostName, "'hostName' cannot be null or empty");

        if(hostName.toUpperCase(Locale.US).startsWith(DeviceModel.SW_HUAWEI.getPrefix()))
            return DeviceModel.SW_HUAWEI;
        else if(hostName.toUpperCase(Locale.US).startsWith(DeviceModel.SW_CISCO.getPrefix()))
            return DeviceModel.SW_CISCO;
        else if(hostName.toUpperCase(Locale.US).startsWith(DeviceModel.OLT_HUAWEI.getPrefix()))
            return DeviceModel.OLT_HUAWEI;
        else if(hostName.toUpperCase(Locale.US).startsWith(DeviceModel.OLT_NOKIA.getPrefix()))
            return DeviceModel.OLT_NOKIA;

        return DeviceModel.UNIDENTIFIED;
    }

    /**
     * to build udp address from targetIP
     * @param ipAddress wanted to be connected
     * @param snmpPort wanted to be connected
     * @return a StringBuilder which is built udp address
     */
    public static StringBuilder buildUdpAddress(String ipAddress, int snmpPort) {
        Assert.hasText(ipAddress, "'ipAddress' cannot be null or empty");

        if(snmpPort == 0)
            snmpPort = DEFAULT_SNMP_PORT;

        char separator = '/';
        StringBuilder udpAddress = new StringBuilder();
        udpAddress.append("udp:").append(ipAddress).append(separator).append(snmpPort);

        return udpAddress;
    }

}
