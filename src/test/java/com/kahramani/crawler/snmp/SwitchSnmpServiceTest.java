package com.kahramani.crawler.snmp;

import com.kahramani.crawler.snmp.application.Application;
import com.kahramani.crawler.snmp.config.PropertyHelper;
import com.kahramani.crawler.snmp.service.SwitchSnmpService;
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
public class SwitchSnmpServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SwitchSnmpServiceTest.class);

    @Autowired
    private PropertyHelper propertyHelper;
    @Autowired
    private SwitchSnmpService switchSnmpService;

    @Test
    public void crawlAllSwitchesOver() {
        this.switchSnmpService.start();
    }

}
