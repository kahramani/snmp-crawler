package com.kahramani.crawler.snmp.config;

import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.mp.SnmpConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;

/**
 * Created by kahramani on 11/22/2016.
 */
@Configuration
@Scope("prototype")
public class SnmpConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SnmpConfiguration.class);
    public static final int DEFAULT_MAX_REPETITIONS = 10;
    public static final int DEFAULT_MAX_SIZE_REQUEST_PDU = 0;
    public static final int DEFAULT_SNMP_RETRY = 2;
    public static final long DEFAULT_SNMP_TIMEOUT = 1000;
    public static final int DEFAULT_SNMP_PDU = PDU.GETBULK;
    public static final int DEFAULT_SNMP_VERSION = SnmpConstants.version2c;

    @Autowired
    private PropertyHelper propertyHelper;

    private String community;
    private int maxSizeRequestPDU;
    private int maxRepetitions;
    private int retry;
    private long timeout;
    private int protocolDataUnit;
    private int snmpVersion;

    protected SnmpConfiguration(PropertyPrefix propertyPrefix) {
        Assert.notNull(propertyPrefix, "'propertyPrefix' cannot be null to configure");

        String prefix = propertyPrefix.get();
        this.community = this.propertyHelper.getString(prefix + ".community");
        this.maxSizeRequestPDU = this.propertyHelper.getInt(prefix + ".max.size.request.pdu",
                this.DEFAULT_MAX_SIZE_REQUEST_PDU);
        this.maxRepetitions = this.propertyHelper.getInt(prefix + ".max.repetitions",
                this.DEFAULT_MAX_REPETITIONS);
        this.retry = this.propertyHelper.getInt(prefix + ".retry",
                this.DEFAULT_SNMP_RETRY);
        this.timeout = this.propertyHelper.getLong(prefix + ".timeOut",
                this.DEFAULT_SNMP_TIMEOUT);
        this.protocolDataUnit = this.DEFAULT_SNMP_PDU;
        this.snmpVersion = this.DEFAULT_SNMP_VERSION;
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
