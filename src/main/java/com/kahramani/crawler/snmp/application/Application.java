package com.kahramani.crawler.snmp.application;

import com.kahramani.crawler.snmp.service.OltSnmpService;
import com.kahramani.crawler.snmp.service.SwitchSnmpService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by kahramani on 11/15/2016.
 */
@ComponentScan("com.kahramani.crawler.*")
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        // run switch snmp service
        SwitchSnmpService switchSnmpService = context.getBean(SwitchSnmpService.class);
        switchSnmpService.start();

        // run olt snmp service
        OltSnmpService oltSnmpService = context.getBean(OltSnmpService.class);
        oltSnmpService.start();

        // destroy all singleton beans
        ((AnnotationConfigApplicationContext) context).destroy();
    }
}
