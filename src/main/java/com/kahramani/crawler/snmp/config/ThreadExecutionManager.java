package com.kahramani.crawler.snmp.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
public class ThreadExecutionManager {

    private int timeout;
    private String threadNamePrefix;
    private ThreadPoolTaskExecutor executor;

    public ThreadExecutionManager(int timeout, String threadNamePrefix) {
        this.timeout = timeout;
        this.threadNamePrefix = threadNamePrefix;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    /**
     * to initialize executor
     * @param corePoolSize executor's core pool size
     */
    private void init(int corePoolSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        if(StringUtils.hasText(this.threadNamePrefix)) {
            executor.setThreadNamePrefix(this.threadNamePrefix);
        }
        executor.setCorePoolSize(corePoolSize);
        if(this.timeout > 0) {
            executor.setAwaitTerminationSeconds(timeout);
            executor.setWaitForTasksToCompleteOnShutdown(true);
        }
        executor.afterPropertiesSet();

        this.executor = executor;
    }

    /**
     * to submit(start) runnables to the thread pool executor
     * @param runnables runnable list wanted to be started
     */
    public void submitRunnables(List<? extends Runnable> runnables) {
        Assert.notEmpty(runnables, "'runnables' cannot be null or empty");
        this.init(runnables.size());
        for (Runnable runnable : runnables) {
            this.executor.submit(runnable);
        }

        this.shutdown();
    }

    /**
     * to shutdown the executor
     */
    public void shutdown() {
        if(this.executor != null) {
            this.executor.shutdown();
            this.executor = null;
        }
    }
}
