package com.kahramani.crawler.snmp.config;

import com.kahramani.crawler.snmp.enums.PropertyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
public class ThreadExecutionManager {

    public static final int DEFAULT_MAX_ACTIVE_THREAD_COUNT = 3;

    @Autowired
    private PropertyHelper propertyHelper;

    private PropertyPrefix propertyPrefix;
    private String namePrefix;
    private ThreadPoolTaskExecutor executor;

    public PropertyPrefix getPropertyPrefix() {
        return propertyPrefix;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public ThreadExecutionManager(PropertyPrefix propertyPrefix, String namePrefix) {
        this.propertyPrefix = propertyPrefix;
        this.namePrefix = namePrefix;
    }

    @PostConstruct
    private void init() {
        Assert.notNull(this.propertyPrefix, "'propertyPrefix' cannot be null to configure the executor manager");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        String prefix = this.propertyPrefix.get();
        int corePoolSize = this.propertyHelper.getInt(prefix + ".thread.max.active.count",
                DEFAULT_MAX_ACTIVE_THREAD_COUNT);
        executor.setCorePoolSize(corePoolSize);
        if(StringUtils.hasText(this.namePrefix)) {
            executor.setThreadNamePrefix(this.namePrefix);
        }
        int timeout = this.propertyHelper.getInt(prefix + ".tasks.timeout", Integer.MAX_VALUE);
        executor.setAwaitTerminationSeconds(timeout);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.afterPropertiesSet();

        this.executor = executor;
    }

    /**
     * to submit(start) runnables to the thread pool executor
     * @param runnables runnable list wanted to be started
     * @param shutdown executer will be shutdown if it is true
     */
    public void submitRunnables(List<? extends Runnable> runnables, boolean shutdown) {
        Assert.notEmpty(runnables, "'runnables' cannot be null or empty");

        for (Runnable runnable : runnables) {
            this.executor.submit(runnable);
        }

        if(shutdown)
            this.shutdown();
    }

    public void shutdown() {
        this.executor.shutdown();
        this.executor = null;
    }
}
