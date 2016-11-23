package com.kahramani.crawler.snmp.action;

import com.kahramani.crawler.snmp.config.PropertyHelper;
import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.models.NetworkElement;
import com.kahramani.crawler.snmp.models.Olt;
import com.kahramani.crawler.snmp.utils.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by kahramani on 11/23/2016.
 */
@Component
@Scope("prototype")
public class OltSnmpTaskRunnable implements SnmpTaskRunnable{

    private static final Logger logger = LoggerFactory.getLogger(OltSnmpTaskRunnable.class);
    private static final int DEFAULT_OLT_MAX_COUNT_TO_INSERT = 1;

    @Autowired
    private PropertyHelper propertyHelper;
    private List<Olt> oltList;

    @Override
    public <T extends NetworkElement> void setList(List<T> neList) {
        this.oltList = (List<Olt>) neList;
    }

    @Override
    public void run() {
        Assert.notEmpty(this.oltList, "'oltList' cannot be null or empty");

        String maxOltCountToInsertKey = PropertyPrefix.OLT_SOURCE_DB_PREFIX + ".max.insert.count.to.db";
        int maxOltCountToInsert = propertyHelper
                .getInt(maxOltCountToInsertKey, DEFAULT_OLT_MAX_COUNT_TO_INSERT);

        if(maxOltCountToInsert <= 0)
            throw new IllegalArgumentException("'" + maxOltCountToInsertKey + "' cannot be 0 or lower");

        Chronometer cr = new Chronometer();
        cr.start();

        logger.info("Thread is started. List size: " + this.oltList.size());

        // TODO STUFF
        cr.stop();
        logger.info("Thread is completed. Run Time: " + cr.getDuration());
        cr.clear();
    }
}
