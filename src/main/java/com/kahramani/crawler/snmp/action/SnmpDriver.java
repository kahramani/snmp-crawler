package com.kahramani.crawler.snmp.action;

import com.kahramani.crawler.snmp.config.PropertyHelper;
import com.kahramani.crawler.snmp.config.SnmpConfiguration;
import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.utils.SnmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kahramani on 11/22/2016.
 */
class SnmpDriver extends SnmpConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SnmpDriver.class);

    private Snmp snmp;
    private TransportMapping transportMapping;

    protected SnmpDriver(PropertyHelper propertyHelper, PropertyPrefix propertyPrefix) {
        super(propertyHelper, propertyPrefix);
    }

    /**
     * to start snmp session for incoming messages
     * @throws IOException if could not be started properly
     */
    protected void startListening() throws IOException {
        if(!isListening())
            this.listen();
    }

    /**
     * to close snmp session
     * @throws IOException if could not be closed properly
     */
    protected void stopListening() throws IOException {
        if(isListening())
            this.close();
    }

    /**
     * to check if snmp session is listening for incoming messages
     * @return a boolean value that implies snmp session is listening for incoming messages or not
     */
    protected boolean isListening() {
        if(this.transportMapping != null)
            return this.transportMapping.isListening();
        return false;
    }

    /**
     * to do walker get and to convert responses to String
     * @param ipAddress ip wanted to operate on
     * @param snmpPort port wanted to operate on
     * @param communityIndex index wanted to get its information
     * @param oidStr oid wanted to get
     * @return a String which is the device response to snmpGet command
     */
    protected String get(String ipAddress, int snmpPort, String communityIndex, String oidStr) {
        Assert.hasText(ipAddress, "'ipAddress' cannot be null or empty");
        Assert.hasText(oidStr, "'oidStr' cannot be null or empty");
        if(snmpPort == 0)
            snmpPort = SnmpUtils.DEFAULT_SNMP_PORT;

        this.setProtocolDataUnit(PDU.GET);

        String response = getResponseEventVariable(ipAddress, snmpPort, communityIndex, oidStr);
//        logger.debug("response is " + response +
//               " for " + this.address(ipAddress, snmpPort) + " - oid: " + oidStr);

        return response;
    }

    /**
     * to get subtree and to convert responses to an oid/value hashMap
     * @param ipAddress ip wanted to operate on
     * @param snmpPort port wanted to operate on
     * @param communityIndex index index wanted to get its information
     * @param rootOid root oid
     * @param maxRepetitions number of the variable binding per event
     * @return a HashMap which holds device response to SNMPWalk command
     */
    protected Map<OID, String> getWalkResponseMap(String ipAddress,
                                                   int snmpPort,
                                                   String communityIndex,
                                                   String rootOid,
                                                   int maxRepetitions) {
        Assert.hasText(ipAddress, "'ipAddress' cannot be null or empty");
        Assert.hasText(rootOid, "'rootOid' cannot be null or empty");
        if(snmpPort == 0)
            snmpPort = SnmpUtils.DEFAULT_SNMP_PORT;

        this.setProtocolDataUnit(PDU.GETBULK);
        List<VariableBinding> variableBindingList =
                getTreeEventVariables(ipAddress, snmpPort, communityIndex, rootOid, maxRepetitions);

        if(variableBindingList == null) {
//                logger.debug("variableBindingList is null" +
//                        " for " + this.address(ipAddress, snmpPort) + " - oid: " + rootOid);
            return null;
        }

        Map<OID, String> responseMap = new HashMap<>();
        for (VariableBinding variableBinding : variableBindingList) {
            responseMap.put(variableBinding.getOid(), variableBinding.getVariable().toString());
        }

        return responseMap;
    }

    /**
     * to get response event variable if there is no error
     * @param ipAddress ip wanted to operate on
     * @param snmpPort port wanted to operate on
     * @param communityIndex wanted to get its information
     * @param oidStr oid wanted to get
     * @return a String which is response event variable
     */
    private String getResponseEventVariable(String ipAddress, int snmpPort, String communityIndex, String oidStr) {
        ResponseEvent responseEvent = getResponseEvent(ipAddress, snmpPort, communityIndex, oidStr);
        if(responseEvent == null) {
//            logger.debug("ResponseEvent is null" +
//                    " for " + this.address(ipAddress, snmpPort) + " - oid: " + oidStr);
            return null;
        }

        PDU pdu = responseEvent.getResponse();
        if(pdu == null) {
//            logger.debug("pdu is null" +
//                    " for " + this.address(ipAddress, snmpPort) + " - oid: " + oidStr);
            return null;
        }

        if (pdu.getErrorStatus() == PDU.noError) {
            return responseEvent.getResponse().get(0).getVariable().toString();
        } else {
            logger.error("Response has an error with " +
                    "status=" + pdu.getErrorStatus() +
                    ", index=" + pdu.getErrorIndex() +
                    ", statusText=" + pdu.getErrorStatusText() +
                    " for " + this.address(ipAddress, snmpPort) + " - oid: " + oidStr);
        }

        return null;
    }

    /**
     * to execute walker command and to get responseEvent object
     * @param ipAddress ip wanted to operate on
     * @param snmpPort port wanted to operate on
     * @param communityIndex wanted to get its information
     * @param oidStr oid wanted to get
     * @return a ResponseEvent object which retrieved from device via walker
     */
    private ResponseEvent getResponseEvent(String ipAddress, int snmpPort, String communityIndex, String oidStr) {
        OID[] oids = new OID[]{new OID(oidStr)};
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(this.getProtocolDataUnit());

        try {
            return this.snmp.send(pdu, buildCommunityTarget(ipAddress, snmpPort, communityIndex));
        } catch (IOException e) {
            logger.error("Failed to get responseEvent " +
                    " from " + this.address(ipAddress, snmpPort) + " - oid: " + oidStr, e);
        }

        return null;
    }

    /**
     * to get tree event variables if there is no error
     * @param ipAddress ip wanted to operate on
     * @param snmpPort port wanted to operate on
     * @param communityIndex wanted to get its information
     * @param rootOid root oid
     * @param maxRepetitions number of the variable binding per event
     * @return an ArrayList of VariableBinding which retrieved by snmpgetBulk
     */
    private List<VariableBinding> getTreeEventVariables(String ipAddress,
                                                            int snmpPort,
                                                            String communityIndex,
                                                            String rootOid,
                                                            int maxRepetitions) {
        List<VariableBinding> eventVariables = null;

        TreeUtils treeUtils = new TreeUtils(this.snmp, new DefaultPDUFactory());
        treeUtils.setMaxRepetitions(maxRepetitions);
        List<TreeEvent> treeEventList = treeUtils.getSubtree(
                buildCommunityTarget(ipAddress, snmpPort, communityIndex),
                new OID(rootOid));

        if (CollectionUtils.isEmpty(treeEventList)) {
//            logger.debug("treeEventList is null or empty" +
//                    " for " + this.address(ipAddress, snmpPort) + " - oid: " + rootOid);
            return eventVariables;
        }

        eventVariables = new ArrayList<>();
        for (TreeEvent treeEvent : treeEventList) {
            if (treeEvent == null) {
                //logger.debug("TreeEvent is null");
            } else {
                if (treeEvent.isError()) {
//                    logger.error("Failed to get event with " +
//                            ", errorMessage=" + treeEvent.getErrorMessage() +
//                            " for " + this.address(ipAddress, snmpPort) + " - oid: " + rootOid);
                } else {
                    VariableBinding[] variableBindings = treeEvent.getVariableBindings();
                    if (ObjectUtils.isEmpty(variableBindings)) {
//                        logger.debug("Variable binding array is null or empty " +
//                                " for " + this.address(ipAddress, snmpPort) + " - oid: " + rootOid);
                    } else {
                        for (VariableBinding variableBinding : variableBindings) {
                            eventVariables.add(variableBinding);
                        }
                    }
                }
            }
        }

        return eventVariables;
    }

    /**
     * to build community target before connecting via walker
     * @param ipAddress ip wanted to start snmp session
     * @param port port wanted to start snmp session
     * @param communityIndex index wanted to get its information
     * @return a CommunityTarget object which holds target features
     */
    private CommunityTarget buildCommunityTarget(String ipAddress, int port, String communityIndex) {
        CommunityTarget communityTarget = new CommunityTarget();

        if(!StringUtils.hasText(communityIndex)) {
            if(this.getCommunity() != null)
                communityTarget.setCommunity(new OctetString(this.getCommunity()));
        } else {
            char separator = '@';
            if(this.getCommunity() != null)
                communityTarget.setCommunity(new OctetString(new StringBuilder(this.getCommunity())
                        .append(separator)
                        .append(communityIndex).toString()));
        }
        communityTarget.setAddress(
                GenericAddress.parse(
                        SnmpUtils.buildUdpAddress(ipAddress, port).toString()));
        communityTarget.setRetries(this.getRetry());
        communityTarget.setTimeout(this.getTimeout());
        communityTarget.setVersion(this.getSnmpVersion());
        if(this.getMaxSizeRequestPDU() != 0)
            communityTarget.setMaxSizeRequestPDU(this.getMaxSizeRequestPDU());

        return communityTarget;
    }

    /**
     * to start snmp session for incoming messages
     * @throws IOException if could not be started properly
     */
    private void listen() throws IOException {
        this.transportMapping = new DefaultUdpTransportMapping();
        this.snmp = new Snmp(this.transportMapping);
        this.transportMapping.listen();
        logger.info("SNMP session is started listening");
    }

    /**
     * to close snmp session
     * @throws IOException if could not be closed properly
     */
    private void close() throws IOException {
        this.transportMapping.close();
        this.snmp.close();
        logger.info("SNMP session is stopped listening");

        this.transportMapping = null;
        this.snmp = null;
    }

    /**
     * to hold address which is currently operated -- Format IP:PORT
     * @param ipAddress device's ip address
     * @param port device's port
     * @return a StringBuilder which tells the device's address
     */
    private StringBuilder address(String ipAddress, int port) {
        char separator = ':';
        return new StringBuilder(ipAddress).append(separator).append(port);
    }
}
