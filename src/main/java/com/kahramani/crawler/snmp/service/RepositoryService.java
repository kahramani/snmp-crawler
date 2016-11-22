package com.kahramani.crawler.snmp.service;

import com.kahramani.crawler.snmp.models.Olt;
import com.kahramani.crawler.snmp.models.OltOntData;
import com.kahramani.crawler.snmp.models.Switch;
import com.kahramani.crawler.snmp.models.SwitchPortData;
import com.kahramani.crawler.snmp.repository.ApplicationRepository;
import com.kahramani.crawler.snmp.repository.OltSourceRepository;
import com.kahramani.crawler.snmp.repository.SwitchSourceRepository;
import com.kahramani.crawler.snmp.utils.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
@Service
public class RepositoryService {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryService.class);

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private SwitchSourceRepository switchSourceRepository;
    @Autowired
    private OltSourceRepository oltSourceRepository;
    @Autowired
    @Qualifier("switchSelectQuery")
    private StringBuilder switchSelectQuery;
    @Autowired
    @Qualifier("oltSelectQuery")
    private StringBuilder oltSelectQuery;
    @Autowired
    @Qualifier("switchInsertQuery")
    private StringBuilder switchInsertQuery;
    @Autowired
    @Qualifier("oltInsertQuery")
    private StringBuilder oltInsertQuery;

    public List<Switch> getSwitchList() {
        Assert.notNull(switchSelectQuery, "'snmp.sw.db.select.sql' must be added to resources");
        Assert.hasText(switchSelectQuery.toString(), "'snmp.sw.db.select.sql' must not be empty");

        Chronometer cr = new Chronometer();
        cr.start();
        List<Switch> switchList = this.switchSourceRepository.getList(switchSelectQuery.toString());
        cr.stop();
        logger.info("Switch list retrieved successfully. Duration: " + cr.getDuration() + ".");

        if(switchList == null)
            return null;

        logger.info("Switch list size: " + switchList.size() + ".");
        cr.clear();

        return switchList;
    }

    public List<Olt> getOltList() {
        Assert.notNull(oltSelectQuery, "'snmp.olt.db.select.sql' must be added to resources");
        Assert.hasText(oltSelectQuery.toString(), "'snmp.olt.db.select.sql' must not be empty");

        Chronometer cr = new Chronometer();
        cr.start();
        List<Olt> oltList = this.oltSourceRepository.getList(oltSelectQuery.toString());
        cr.stop();
        logger.info("Olt list retrieved successfully. Duration: " + cr.getDuration() + ".");

        if(oltList == null)
            return null;

        logger.info("Olt list size: " + oltList.size() + ".");
        cr.clear();

        return oltList;
    }

    public int[] insertSwitchPortDataList(final List<SwitchPortData> portDataList) {
        Assert.notNull(switchInsertQuery, "'snmp.sw.db.insert.sql' must be added to resources");
        Assert.hasText(switchInsertQuery.toString(), "'snmp.sw.db.insert.sql' must not be empty");

        Chronometer cr = new Chronometer();
        cr.start();
        int[] insertionCount = this.applicationRepository
                .insertSwitchPortDataList(switchInsertQuery.toString(), portDataList);
        cr.stop();
        logger.info("Switch port data list inserted successfully. Duration: " + cr.getDuration() + ".");

        if(insertionCount == null)
            return null;

        logger.info("Switch port data insertion count: " + insertionCount.length + ".");
        cr.clear();

        return insertionCount;
    }

    public int[] insertOltOntDataList(final List<OltOntData> ontDataList) {
        Assert.notNull(oltInsertQuery, "'snmp.olt.db.insert.sql' must be added to resources");
        Assert.hasText(oltInsertQuery.toString(), "'snmp.olt.db.insert.sql' must not be empty");

        Chronometer cr = new Chronometer();
        cr.start();
        int[] insertionCount = this.applicationRepository
                .insertOltOntDataList(oltInsertQuery.toString(), ontDataList);
        cr.stop();
        logger.info("Olt ont data list inserted successfully. Duration: " + cr.getDuration() + ".");

        if(insertionCount == null)
            return null;

        logger.info("Olt ont data insertion count: " + insertionCount.length + ".");
        cr.clear();

        return insertionCount;
    }
}
