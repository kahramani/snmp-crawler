package com.kahramani.crawler.snmp.config;

import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import org.snmp4j.PDU;
import org.snmp4j.mp.SnmpConstants;
import org.springframework.util.Assert;

/**
 * Created by kahramani on 11/22/2016.
 */
public class SnmpConfiguration {

    public static final int DEFAULT_MAX_REPETITIONS = 10;
    public static final int DEFAULT_MAX_SIZE_REQUEST_PDU = 0;
    public static final int DEFAULT_SNMP_RETRY = 2;
    public static final long DEFAULT_SNMP_TIMEOUT = 1000;
    public static final int DEFAULT_SNMP_PDU = PDU.GETBULK;
    public static final int DEFAULT_SNMP_VERSION = SnmpConstants.version2c;

    private String community;
    private int maxSizeRequestPDU;
    private int maxRepetitions;
    private int retry;
    private long timeout;
    private int protocolDataUnit;
    private int snmpVersion;

    protected SnmpConfiguration(PropertyHelper propertyHelper, PropertyPrefix propertyPrefix) {
        Assert.notNull(propertyPrefix, "'propertyPrefix' cannot be null to configure");

        String prefix = propertyPrefix.get();
        this.community = propertyHelper.getString(prefix + ".community");
        this.maxSizeRequestPDU = propertyHelper.getInt(prefix + ".max.size.request.pdu",
                DEFAULT_MAX_SIZE_REQUEST_PDU);
        this.maxRepetitions = propertyHelper.getInt(prefix + ".max.repetitions",
                DEFAULT_MAX_REPETITIONS);
        this.retry = propertyHelper.getInt(prefix + ".retry",
                DEFAULT_SNMP_RETRY);
        this.timeout = propertyHelper.getLong(prefix + ".timeOut",
                DEFAULT_SNMP_TIMEOUT);
        this.protocolDataUnit = DEFAULT_SNMP_PDU;
        this.snmpVersion = DEFAULT_SNMP_VERSION;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public int getMaxSizeRequestPDU() {
        return maxSizeRequestPDU;
    }

    public void setMaxSizeRequestPDU(int maxSizeRequestPDU) {
        this.maxSizeRequestPDU = maxSizeRequestPDU;
    }

    public int getMaxRepetitions() {
        return maxRepetitions;
    }

    public void setMaxRepetitions(int maxRepetitions) {
        this.maxRepetitions = maxRepetitions;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getProtocolDataUnit() {
        return protocolDataUnit;
    }

    public void setProtocolDataUnit(int protocolDataUnit) {
        this.protocolDataUnit = protocolDataUnit;
    }

    public int getSnmpVersion() {
        return snmpVersion;
    }

    public void setSnmpVersion(int snmpVersion) {
        this.snmpVersion = snmpVersion;
    }
}
