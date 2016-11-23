package com.kahramani.crawler.snmp.action;

import com.kahramani.crawler.snmp.config.PropertyHelper;
import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.models.NetworkElement;
import com.kahramani.crawler.snmp.models.Switch;
import com.kahramani.crawler.snmp.utils.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
    private List<Switch> switchList;

    @Override
    public <T extends NetworkElement> void setList(List<T> neList) {
        this.switchList = (List<Switch>) neList;
    }

    @Override
    public void run() {
        Assert.notEmpty(this.switchList, "'switchList' cannot be null or empty");

        String maxSwitchCountToInsertKey = PropertyPrefix.SW_SOURCE_DB_PREFIX + ".max.insert.count.to.db";
        int maxSwitchCountToInsert = propertyHelper
                .getInt(maxSwitchCountToInsertKey, DEFAULT_SWITCH_MAX_COUNT_TO_INSERT);

        if(maxSwitchCountToInsert <= 0)
            throw new IllegalArgumentException("'" + maxSwitchCountToInsertKey + "' cannot be 0 or lower");

        Chronometer cr = new Chronometer();
        cr.start();

        logger.info("Thread is started. List size: " + this.switchList.size());

        // TODO STUFF
        cr.stop();
        logger.info("Thread is completed. Run Time: " + cr.getDuration());
        cr.clear();
    }
}
