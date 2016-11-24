package com.kahramani.crawler.snmp.action;

import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.models.NetworkElement;
import com.kahramani.crawler.snmp.models.Switch;
import com.kahramani.crawler.snmp.models.SwitchPortData;
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
public class SwitchSnmpCrawler extends SnmpDriver implements SnmpCrawler {

    private static Logger logger = LoggerFactory.getLogger(SwitchSnmpCrawler.class);

    protected SwitchSnmpCrawler() {
        super(PropertyPrefix.SW_PREFIX);
    }

    @Override
    public List<SwitchPortData> crawlOver(NetworkElement ne) {
        Assert.notNull(ne, "'ne' cannot be null");
        Switch sw = (Switch) ne;
        SwitchPortData switchPortData = new SwitchPortData(sw);
        logger.info("Process started for address: " + this.address(sw));
        // TODO snmp walk stuff

        List<SwitchPortData> portDataList = new ArrayList<>();
        portDataList.add(switchPortData);
        logger.info("Process ended for address: " + this.address(sw));
        return portDataList;
    }

    public List<SwitchPortData> crawlAllOver(List neList) {
        Assert.notEmpty(neList, "'neList' cannot be null or empty to run this operation");

        try {
            this.startListening();
        } catch (IOException e) {
            logger.error("Failed to start listening for snmp session", e);
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
            try {
                this.stopListening();
            } catch (IOException e) {
                logger.error("Failed to stop listening for snmp session", e);
            }
        }

        logger.info("Process ended for list size with " + neList.size());
        return cumulativePortDataList;
    }

    @Override
    public StringBuilder address(NetworkElement ne) {
        char separator = '/';
        return new StringBuilder(ne.getIpAddress()).append(separator).append(ne.getHostName());
    }
}
