package com.cxylm.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 队列配置
 *
 * @author Zhangzhe
 * 2020年1月2日 18:07:39
 */
@Configuration
@Slf4j
public class QueueConfig {

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate) {
        return new AsyncRabbitTemplate(rabbitTemplate);
    }
}
