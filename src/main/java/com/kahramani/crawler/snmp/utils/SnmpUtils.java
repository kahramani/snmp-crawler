package com.kahramani.crawler.snmp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.OID;
import org.springframework.util.Assert;

/**
 * Created by kahramani on 11/22/2016.
 */
public class SnmpUtils {

    private static final Logger logger = LoggerFactory.getLogger(SnmpUtils.class);
    public static final int DEFAULT_SNMP_PORT = 161;

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
