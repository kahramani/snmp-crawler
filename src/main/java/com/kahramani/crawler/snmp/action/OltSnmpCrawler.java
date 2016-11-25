package com.kahramani.crawler.snmp.action;

import com.kahramani.crawler.snmp.config.PropertyHelper;
import com.kahramani.crawler.snmp.enums.DeviceModel;
import com.kahramani.crawler.snmp.enums.OIDStructure;
import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.models.NetworkElement;
import com.kahramani.crawler.snmp.models.Olt;
import com.kahramani.crawler.snmp.models.OltOntData;
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
class OltSnmpCrawler extends SnmpDriver implements SnmpCrawler {

    private static Logger logger = LoggerFactory.getLogger(OltSnmpCrawler.class);

    protected OltSnmpCrawler(PropertyHelper propertyHelper) {
        super(propertyHelper, PropertyPrefix.OLT_PREFIX);
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
     * to obtain data from the given olt via snmp
     * @param ne olt wanted to obtain data
     * @return a List of OltOntData which holds data obtained from the given olt
     */
    @Override
    public List<OltOntData> crawlOver(NetworkElement ne) {
        Assert.notNull(ne, "'ne' cannot be null");
        Assert.hasText(ne.getIpAddress(), "'ipAddress' cannot be null or empty");
        Assert.notNull(ne.getDeviceModel(), "'deviceModel' cannot be null");

        boolean sessionCreated = false;
        if(!this.isListening()) { // if session do not open, create a session
            this.createSession();
            sessionCreated = true;
        }

        List<OltOntData> ontDataList;
        Olt olt = (Olt) ne;
        logger.info("Process started for address: " + this.address(olt));
        // TODO snmp walk stuff

        // ------ SAMPLE STARTED ------
        // get uptime,
        // if uptime can be retrieved it means that olt is up
        // get ont serial number on olt
        String ipAddress = ne.getIpAddress();
        int port = SnmpUtils.DEFAULT_SNMP_PORT;
        int maxRepetitions = this.getMaxRepetitions();
        String deviceUptime = this.get(ipAddress, port, null, OIDStructure.UPTIME.get());

        if(StringUtils.hasText(deviceUptime)) {
            olt.setIsReachable(true);
            logger.info("Could not be reached to " + this.address(olt));
        }

        ontDataList = new ArrayList<>();
        OltOntData ontData = new OltOntData(olt);

        if(olt.isReachable()) {
            String oid = "";

            if (DeviceModel.OLT_HUAWEI == olt.getDeviceModel())
                oid = OIDStructure.OLT_HUAWEI_ONT_SERIAL_NUMBER.get();
            else if (DeviceModel.OLT_NOKIA == olt.getDeviceModel())
                oid = OIDStructure.OLT_NOKIA_ONT_SERIAL_NUMBER.get();

            Map<OID, String> ontSerialNumberMap = this.getWalkResponseMap(ipAddress, port, null, oid, maxRepetitions);
            // ...
            // ...
            // ...
            ontData.setSlot("");
            ontData.setPort("");
            ontData.setOntNo("");
            ontData.setSerialNumber("");
        }
        ontDataList.add(ontData);
        // ------ SAMPLE ENDED ------

        if(sessionCreated) // if session created here, so terminate session here
            this.terminateSession();
        logger.info("Process ended for address: " + this.address(olt));
        return ontDataList;
    }

    /**
     * to obtain data from the given olt list via snmp
     * @param neList olt list wanted to obtain data
     * @return a List of OltOntData which holds data obtained from the given olt list
     */
    @Override
    public List<OltOntData> crawlAllOver(List neList) {
        Assert.notEmpty(neList, "'neList' cannot be null or empty to run this operation");

        if(!this.createSession()) {
            return null;
        }
        logger.info("Process started for list size with " + neList.size());

        List<OltOntData> cumulativeOntDataList = new ArrayList<>();
        try {
            for(Object ne : neList) {
                if(ne != null) {
                    List<OltOntData> ontDataList = crawlOver((NetworkElement) ne);

                    if(!CollectionUtils.isEmpty(ontDataList))
                        cumulativeOntDataList.addAll(ontDataList);
                }
            }
        } finally {
            this.terminateSession();
        }

        logger.info("Process ended for list size with " + neList.size());
        return cumulativeOntDataList;
    }

    /**
     * to get olt address which is currently operated -- Format IP:HostName
     * @param ne olt which is currently operated
     * @return a StringBuilder which is the olt address
     */
    @Override
    public StringBuilder address(NetworkElement ne) {
        char separator = '/';
        return new StringBuilder(ne.getIpAddress()).append(separator).append(ne.getHostName());
    }

}
