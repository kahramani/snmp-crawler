package com.kahramani.crawler.snmp.service;

import com.kahramani.crawler.snmp.action.SwitchSnmpTaskRunnable;
import com.kahramani.crawler.snmp.config.ThreadExecutionManager;
import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.models.Switch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
@Service("switchSnmpService")
public class SwitchSnmpService extends SnmpServiceAbstract {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SnmpTaskGenerator snmpTaskGenerator;

    public SwitchSnmpService() {
        super(SwitchSnmpService.class);
    }

    @Override
    protected void runService() {
        // get switch list to crawl over
        List<Switch> switchList = repositoryService.getSwitchList();

        // create runnables from the list for thread execution
        List<SwitchSnmpTaskRunnable> switchSnmpTaskRunnables =
                snmpTaskGenerator.generate(PropertyPrefix.SW_PREFIX, switchList, SwitchSnmpTaskRunnable.class);

        // start executing threads
        String threadNamePrefix = "SwitchSnmpThread_";
        int timeOut = this.propertyHelper.getInt(PropertyPrefix.SW_PREFIX.get() + ".crawler.timeout", Integer.MAX_VALUE);
        ThreadExecutionManager manager = new ThreadExecutionManager(timeOut, threadNamePrefix);
        manager.submitRunnables(switchSnmpTaskRunnables);
    }
}
