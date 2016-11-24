package com.kahramani.crawler.snmp.action;

import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.models.NetworkElement;
import com.kahramani.crawler.snmp.models.Olt;
import com.kahramani.crawler.snmp.models.OltOntData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kahramani on 11/24/2016.
 */
public class OltSnmpCrawler extends SnmpDriver implements SnmpCrawler {

    private static Logger logger = LoggerFactory.getLogger(OltSnmpCrawler.class);

    protected OltSnmpCrawler() {
        super(PropertyPrefix.OLT_PREFIX);
    }

    @Override
    public List<OltOntData> crawlOver(NetworkElement ne) {
        Assert.notNull(ne, "'ne' cannot be null");
        Olt sw = (Olt) ne;
        OltOntData ontData = new OltOntData(sw);
        logger.info("Process started for address: " + this.address(sw));
        // TODO snmp walk stuff

        List<OltOntData> ontDataList = new ArrayList<>();
        ontDataList.add(ontData);
        logger.info("Process ended for address: " + this.address(sw));
        return ontDataList;
    }


    public List<OltOntData> crawlAllOver(List neList) {
        Assert.notEmpty(neList, "'neList' cannot be null or empty to run this operation");

        try {
            this.startListening();
        } catch (IOException e) {
            logger.error("Failed to start listening for snmp session", e);
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
            try {
                this.stopListening();
            } catch (IOException e) {
                logger.error("Failed to stop listening for snmp session", e);
            }
        }

        logger.info("Process ended for list size with " + neList.size());
        return cumulativeOntDataList;
    }

    @Override
    public StringBuilder address(NetworkElement ne) {
        char separator = '/';
        return new StringBuilder(ne.getIpAddress()).append(separator).append(ne.getHostName());
    }

}
