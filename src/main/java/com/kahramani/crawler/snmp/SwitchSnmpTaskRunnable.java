package com.kahramani.crawler.snmp;

import com.kahramani.crawler.snmp.config.PropertyHelper;
import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.models.NetworkElement;
import com.kahramani.crawler.snmp.models.Switch;
import com.kahramani.crawler.snmp.models.SwitchPortData;
import com.kahramani.crawler.snmp.service.RepositoryService;
import com.kahramani.crawler.snmp.utils.Chronometer;
import com.kahramani.crawler.snmp.utils.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
@Component
@Scope("prototype")
public class SwitchSnmpTaskRunnable implements SnmpTaskRunnable {

    private static final Logger logger = LoggerFactory.getLogger(SwitchSnmpTaskRunnable.class);
    private static final int DEFAULT_SWITCH_MAX_COUNT_TO_INSERT = 20;

    @Autowired
    private PropertyHelper propertyHelper;
    @Autowired
    private RepositoryService repositoryService;

    private List<Switch> switchList;

    @Override
    public <T extends NetworkElement> void setList(List<T> neList) {
        this.switchList = (List<Switch>) neList;
    }

    @Override
    public void run() {
        Assert.notEmpty(this.switchList, "'switchList' cannot be null or empty");

        String maxSwitchCountToInsertKey = PropertyPrefix.SW_PREFIX + ".max.insert.count.to.db";
        int maxSwitchCountToInsert = propertyHelper
                .getInt(maxSwitchCountToInsertKey, DEFAULT_SWITCH_MAX_COUNT_TO_INSERT);

        Assert.isTrue(maxSwitchCountToInsert > 0, "'" + maxSwitchCountToInsertKey + "' cannot be 0 or lower");

        Chronometer cr = new Chronometer();
        cr.start();

        logger.info("Thread is started. List size: " + this.switchList.size());

        List<List<?>> splitList = ListUtils.splitListByPartitionSize(this.switchList, maxSwitchCountToInsert);

        Assert.notEmpty(splitList, "'splitList' could not be created");

        SwitchSnmpCrawler crawler = new SwitchSnmpCrawler(this.propertyHelper);
        for(List<?> partition : splitList) {
            List<SwitchPortData> portDataList = crawler.crawlAllOver(partition);

            if(!CollectionUtils.isEmpty(portDataList))
                repositoryService.insertSwitchPortDataList(portDataList);
        }

        cr.stop();

        logger.info("Thread is completed. Run Time: " + cr.getDuration());
        cr.clear();
    }
}
