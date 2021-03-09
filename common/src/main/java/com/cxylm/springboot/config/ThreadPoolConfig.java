package com.cxylm.springboot.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

@Configuration
@EnableAsync
public class ThreadPoolConfig {
    private static final int THREADS = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * Java中的线程池和异步任务详解 https://blog.csdn.net/fanrenxiang/article/details/79855992
     */
    @Bean
    public ExecutorService cxylmTaskExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNamePrefix("cxylm-thread-pool-").build();
        return new ThreadPoolExecutor(THREADS, 2 * THREADS,
                60, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(4096),
                threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

//    ThreadPoolExecutor 还允许你自定义当添加任务失败后的执行策略。你可以调用线程池的 setRejectedExecutionHandler() 方法，用自定义的 RejectedExecutionHandler 对象替换现有的策略。
//    ThreadPoolExecutor 提供 4 个现有的策略，分别是：
//    ThreadPoolExecutor.AbortPolicy：表示拒绝任务并抛出异常
//　　ThreadPoolExecutor.DiscardPolicy：表示拒绝任务但不做任何动作
//　　ThreadPoolExecutor.CallerRunsPolicy：表示拒绝任务，并在调用者的线程中直接执行该任务
//　　ThreadPoolExecutor.DiscardOldestPolicy：表示先丢弃任务队列中的第一个任务，然后把这个任务加进队列。
}
