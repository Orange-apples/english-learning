package com.cxylm.springboot;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@MapperScan(basePackages = {"com.cxylm.springboot.dao"})
//@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
//        basePackages = {"com.cxylm.springboot.repository.redis"})
// Elasticsearch数据库 Repo 配置，启用请放开注释
//@EnableElasticsearchRepositories(basePackages = {"com.cxylm.springboot.repository.es"})
@EnableTransactionManagement
public class SysApplication {
    private final static Logger log = LoggerFactory.getLogger(SysApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SysApplication.class, args);
        log.info("启动完毕");
    }

    @Bean
    public GracefulShutdownTomcat gracefulShutdownTomcat() {
        return new GracefulShutdownTomcat();
    }

    @Bean
    public ServletWebServerFactory servletContainer(GracefulShutdownTomcat gracefulShutdownTomcat) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(gracefulShutdownTomcat);
        return tomcat;
    }

    private static class GracefulShutdownTomcat implements TomcatConnectorCustomizer,
            ApplicationListener<ContextClosedEvent> {

        private static final Logger log = LoggerFactory.getLogger(GracefulShutdownTomcat.class);

        private volatile Connector connector;

        @Override
        public void customize(Connector connector) {
            this.connector = connector;
        }

        long timeWait = 30;

        @Override
        public void onApplicationEvent(@NotNull ContextClosedEvent event) {
            this.connector.pause();
            log.info("Tomcat connector paused");
            Executor executor = this.connector.getProtocolHandler().getExecutor();
            if (executor instanceof ThreadPoolExecutor) {
                try {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                    threadPoolExecutor.shutdown();
                    if (!threadPoolExecutor.awaitTermination(timeWait, TimeUnit.SECONDS)) {
                        log.warn("Tomcat thread pool did not shut down gracefully within " + timeWait + " seconds. Proceeding with forceful shutdown");
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            log.info("Tomcat thread pool shut down gracefully");
        }

    }
}
