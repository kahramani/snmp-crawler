package com.kahramani.crawler.snmp;

import com.kahramani.crawler.snmp.config.PropertyHelper;
import com.kahramani.crawler.snmp.enums.DeviceModel;
import com.kahramani.crawler.snmp.enums.OIDStructure;
import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.models.NetworkElement;
import com.kahramani.crawler.snmp.models.Switch;
import com.kahramani.crawler.snmp.models.SwitchPortData;
import com.kahramani.crawler.snmp.utils.SnmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.OID;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kahramani on 11/24/2016.
 */
class SwitchSnmpCrawler extends SnmpDriver implements SnmpCrawler {

    private static Logger logger = LoggerFactory.getLogger(SwitchSnmpCrawler.class);

    protected SwitchSnmpCrawler(PropertyHelper propertyHelper) {
        super(propertyHelper, PropertyPrefix.SW_PREFIX);
    }

    /**
     * to create session
     * @return a boolean value which is the flag of operation is success or not
     */
    @Override
    public boolean createSession() {
        try {
            this.startListening();
        } catch (IOException e) {
            logger.error("Failed to start listening for snmp session", e);
            return false;
        }
        return true;
    }

    /**
     * to terminate current session
     * @return a boolean value which is the flag of operation is success or not
     */
    @Override
    public boolean terminateSession() {
        try {
            this.stopListening();
        } catch (IOException e) {
            logger.error("Failed to stop listening for snmp session", e);
            return false;
        }
        return true;
    }

    /**
     * to obtain data from the given switch via snmp
     * @param ne switch wanted to obtain data
     * @return a List of SwitchPortData which holds data obtained from the given switch
     */
    @Override
    public List<SwitchPortData> crawlOver(NetworkElement ne) {
        Assert.notNull(ne, "'ne' cannot be null");
        Assert.hasText(ne.getIpAddress(), "'ipAddress' cannot be null or empty");
        Assert.notNull(ne.getDeviceModel(), "'deviceModel' cannot be null");

        boolean sessionCreated = false;
        if(!this.isListening()) { // if session do not open, create a session
            this.createSession();
            sessionCreated = true;
        }

        List<SwitchPortData> portDataList;
        Switch sw = (Switch) ne;
        logger.info("Process started for address: " + this.address(sw));

        // TODO snmp walk stuff
        // ------ SAMPLE STARTED ------
        // get uptime,
        // if uptime can be retrieved it means that switch is up
        // get vlans on switch
        String ipAddress = sw.getIpAddress();
        int port = SnmpUtils.DEFAULT_SNMP_PORT;
        int maxRepetitions = this.getMaxRepetitions();
        String deviceUptime = this.get(ipAddress, port, null, OIDStructure.UPTIME.get());

        if(StringUtils.hasText(deviceUptime)) {
            sw.setIsReachable(true);
            logger.info("Could not be reached to " + this.address(sw));
        }

        portDataList = new ArrayList<>();
        SwitchPortData portData = new SwitchPortData(sw);

        if(sw.isReachable()) {
            String oid = "";

            if (DeviceModel.SW_HUAWEI == sw.getDeviceModel())
                oid = OIDStructure.SW_HUAWEI_VLAN.get();
            else if (DeviceModel.SW_CISCO == sw.getDeviceModel())
                oid = OIDStructure.SW_CISCO_VLAN.get();

            Map<OID, String> vlanMap = this.getWalkResponseMap(ipAddress, port, null, oid, maxRepetitions);
            // ...
            // ...
            // ...
            portData.setVlan("");
            portData.setPort("");
            portData.setMacAddress("");
        }
        portDataList.add(portData);
        // ------ SAMPLE ENDED ------

        if(sessionCreated) // if session created here, so terminate session here
            this.terminateSession();
        logger.info("Process ended for address: " + this.address(sw));
        return portDataList;
    }

    /**
     * to obtain data from the given switch list via snmp
     * @param neList switch list wanted to obtain data
     * @return a List of SwitchPortData which holds data obtained from the given switch list
     */
    @Override
    public List<SwitchPortData> crawlAllOver(List neList) {
        Assert.notEmpty(neList, "'neList' cannot be null or empty to run this operation");

        if(!this.createSession()) {
            return null;
        }
        logger.info("Process started for list size with " + neList.size());

        List<SwitchPortData> cumulativePortDataList = new ArrayList<>();
        try {
            for(Object ne : neList) {
                if(ne != null) {
                    List<SwitchPortData> portDataList = crawlOver((NetworkElement) ne);

                    if(!CollectionUtils.isEmpty(portDataList))
                        cumulativePortDataList.addAll(portDataList);
                }
            }
        } finally {
            this.terminateSession();
        }

        logger.info("Process ended for list size with " + neList.size());
        return cumulativePortDataList;
    }

    /**
     * to get switch address which is currently operated -- Format IP:HostName
     * @param ne switch which is currently operated
     * @return a StringBuilder which is the switch address
     */
    @Override
    public StringBuilder address(NetworkElement ne) {
        char separator = '/';
        return new StringBuilder(ne.getIpAddress()).append(separator).append(ne.getHostName());
    }
}
