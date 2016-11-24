package com.kahramani.crawler.snmp.service;

import com.kahramani.crawler.snmp.action.OltSnmpTaskRunnable;
import com.kahramani.crawler.snmp.config.ThreadExecutionManager;
import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import com.kahramani.crawler.snmp.models.Olt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kahramani on 11/23/2016.
 */
@Service("oltSnmpService")
public class OltSnmpService extends SnmpServiceAbstract {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SnmpTaskGenerator snmpTaskGenerator;

    public OltSnmpService() {
        super(OltSnmpService.class);
    }

    @Override
    protected void runService(com.kahramani.crawler.snmp.enums.Process process) {
        // get olt list to crawl over
        List<Olt> oltList = repositoryService.getOltList();

        // create runnables from the list for thread execution
        List<OltSnmpTaskRunnable> oltSnmpTaskRunnables =
                snmpTaskGenerator.generate(PropertyPrefix.SW_PREFIX, oltList, OltSnmpTaskRunnable.class);

        // start executing threads
        String threadNamePrefix = "OltSnmpThread_";
        ThreadExecutionManager manager = new ThreadExecutionManager(PropertyPrefix.SW_PREFIX, threadNamePrefix);
        manager.submitRunnables(oltSnmpTaskRunnables, true);
    }
}
