package com.kahramani.crawler.snmp;

import com.kahramani.crawler.snmp.application.Application;
import com.kahramani.crawler.snmp.enums.Process;
import com.kahramani.crawler.snmp.service.SnmpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by kahramani on 11/23/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class SnmpCrawlerTest {

    private static final Logger logger = LoggerFactory.getLogger(SnmpCrawlerTest.class);

    @Autowired
    private SnmpService snmpService;

    @Test
    public void crawlAllSwitchesOver() {
        this.snmpService.start(Process.SWITCH_SNMP);
    }

    @Test
    public void crawlAllOltsOver() {
        this.snmpService.start(Process.OLT_SNMP);
    }
}
