package com.cxylm.springboot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
    private final ExecutorService cxylmTaskExecutor;

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        log.info("Shutting down thread pool...");
        try {
            cxylmTaskExecutor.shutdown();
            if (!cxylmTaskExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("Tomcat thread pool did not shut down gracefully within "
                        + "5 seconds. Proceeding with forceful shutdown");
                final List<Runnable> task = cxylmTaskExecutor.shutdownNow();
                log.warn("There are {} task(s) in cxylmTaskExecutor awaiting execution, lost them all", task.size());
            }
            log.info("TaskExecutor cxylmTaskExecutor shutdown successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error await cxylmTaskExecutor shutdown", e);

        }
    }
}
