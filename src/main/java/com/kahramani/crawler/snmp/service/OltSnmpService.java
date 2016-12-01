package com.kahramani.crawler.snmp.service;

import com.kahramani.crawler.snmp.OltSnmpTaskRunnable;
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
    private SnmpServiceTaskGenerator taskGenerator;

    public OltSnmpService() {
        super(OltSnmpService.class);
    }

    @Override
    protected void runService() {
        // get olt list to crawl over
        List<Olt> oltList = repositoryService.getOltList();

        // create runnables from the list for thread execution
        List<OltSnmpTaskRunnable> oltSnmpTaskRunnables =
                taskGenerator.generate(PropertyPrefix.OLT_PREFIX, oltList, OltSnmpTaskRunnable.class);

        // start executing threads
        String threadNamePrefix = "OltSnmpThread_";
        int timeOut = this.propertyHelper.getInt(PropertyPrefix.OLT_PREFIX.get() + ".crawler.timeout", Integer.MAX_VALUE);
        ThreadExecutionManager manager = new ThreadExecutionManager(timeOut, threadNamePrefix);
        manager.submitRunnables(oltSnmpTaskRunnables);
    }
}
