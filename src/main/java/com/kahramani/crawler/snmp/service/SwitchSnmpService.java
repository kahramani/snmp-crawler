package com.kahramani.crawler.snmp.service;

import com.kahramani.crawler.snmp.action.SnmpTaskRunnable;
import com.kahramani.crawler.snmp.action.SnmpTaskGenerator;
import com.kahramani.crawler.snmp.action.SwitchSnmpTaskRunnable;
import com.kahramani.crawler.snmp.config.ThreadExecutionManager;
import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.models.Switch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
@Service("switchSnmpService")
class SwitchSnmpService extends SnmpService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SnmpTaskGenerator snmpTaskGenerator;

    public SwitchSnmpService() {
        super(SwitchSnmpService.class);
    }

    @Override
    protected void runService(com.kahramani.crawler.snmp.enums.Process process) {
        // get switch list to crawl over
        List<Switch> switchList = repositoryService.getSwitchList();

        // create runnables from the list for thread execution
        List<? extends SnmpTaskRunnable> switchSnmpTaskRunnables =
                snmpTaskGenerator.generate(PropertyPrefix.SW_PREFIX, switchList, SwitchSnmpTaskRunnable.class);

        // start executing threads
        String threadNamePrefix = "SwitchSnmpThread_";
        ThreadExecutionManager manager = new ThreadExecutionManager(PropertyPrefix.SW_PREFIX, threadNamePrefix);
        manager.submitRunnables(switchSnmpTaskRunnables, true);
    }
}
