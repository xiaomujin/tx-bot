package com.neko.txbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {
    public static final int cpuNum = Runtime.getRuntime().availableProcessors();

    @Bean("botTaskExecutor")
    public ThreadPoolTaskExecutor listenerExecutor() {
        ThreadPoolTaskExecutor listenerExecutor = new ThreadPoolTaskExecutor();
        //设置线程池参数信息
        listenerExecutor.setCorePoolSize(cpuNum);
        listenerExecutor.setMaxPoolSize(cpuNum * 2);
        listenerExecutor.setQueueCapacity(200);
        listenerExecutor.setKeepAliveSeconds(60);
        listenerExecutor.setThreadNamePrefix("bot-task-");
        listenerExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        listenerExecutor.setAwaitTerminationSeconds(60);
        //修改拒绝策略为使用当前线程执行
        listenerExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化线程池
        listenerExecutor.initialize();
        return listenerExecutor;
    }

}
