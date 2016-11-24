package com.kahramani.crawler.snmp.service;

import com.kahramani.crawler.snmp.config.PropertyHelper;
import com.kahramani.crawler.snmp.enums.Process;
import com.kahramani.crawler.snmp.utils.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kahramani on 11/22/2016.
 */
public abstract class SnmpService {

    private static Logger logger;

    @Autowired
    protected PropertyHelper propertyHelper;

    public SnmpService(Class c) {
        this.logger =  LoggerFactory.getLogger(c);
    }

    public void start(Process process) {
        String processName = process.getName();
        logger.info(processName + " Service started.");
        Chronometer cr = new Chronometer();
        cr.start();
        runService(process);
        cr.stop();
        logger.info(processName + " Service run time : " + cr.getDuration() + ".");
        cr.clear();
    }

    protected abstract void runService(Process process);
}
