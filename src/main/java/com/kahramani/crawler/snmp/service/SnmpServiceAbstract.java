package com.kahramani.crawler.snmp.service;

import com.kahramani.crawler.snmp.config.PropertyHelper;
import com.kahramani.crawler.snmp.utils.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kahramani on 11/22/2016.
 */
abstract class SnmpServiceAbstract {

    private static Logger logger;

    @Autowired
    protected PropertyHelper propertyHelper;

    public SnmpServiceAbstract(Class c) {
        this.logger =  LoggerFactory.getLogger(c);
    }

    public void start() {
        logger.info("Service started.");
        Chronometer cr = new Chronometer();
        cr.start();
        runService();
        cr.stop();
        logger.info("Service ended. Run time : " + cr.getDuration() + ".");
        cr.clear();
    }

    protected abstract void runService();
}
