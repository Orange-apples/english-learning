package com.cxylm.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @auther Orange-apples
 * @date 2021/3/15 21:51
 */
@Configuration
public class ScheduleServiceConfig {
    private static final Integer CORE_POOL_SIZE = 10;

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(10);
    }
}
