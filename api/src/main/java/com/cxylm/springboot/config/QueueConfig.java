package com.cxylm.springboot.config;

import com.alibaba.fastjson.JSON;
import com.cxylm.springboot.constant.Constant;
import com.cxylm.springboot.enums.MQFailType;
import com.cxylm.springboot.model.MQFailLog;
import com.cxylm.springboot.service.mq.MQRetryMsg;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
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
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(QueueConfig.class);

    public static final String QUEUE_SMS = "cxylm.sms";
    public static final String QUEUE_WX_PUSH = "cxylm.wx.push";
    public final static String QUEUE_ORDER_EXPIRE ="cxylm.order.expire";

    public static final String COMMON_DELAY_EXCHANGE = "cxylm.delay.common";

    public static final String EXCHANGE_ES = "cxylm.es";

    public static final String ROUTING_KEY_ES_WALLET_RECORD = "es.wallet-record";
    public static final String ROUTING_KEY_ES_PAY_ORDER = "es.pay-order";
    public static final String ROUTING_KEY_INVITE_MEMBER_EXPIRE = "member.invite.expire";

    @Bean
    Exchange commonDelayExchange() {
        return new ExchangeBuilder(COMMON_DELAY_EXCHANGE, "x-delayed-message")
                .withArgument("x-delayed-type", "topic")
                .build();
    }

    @Bean
    public Queue smsQueue() {
        return QueueBuilder.nonDurable(QUEUE_SMS).build();
    }

    @Bean
    public Queue orderExpire() {
        return QueueBuilder.durable(QUEUE_ORDER_EXPIRE).build();
    }

//    @Bean
//    TopicExchange orderExchange() {
//        return (TopicExchange) ExchangeBuilder.topicExchange(ORDER_EXCHANGE_NAME).build();
//    }

    @Bean
    Binding orderDelayExchangeBinding(Exchange commonDelayExchange, Queue orderExpire) {
        return BindingBuilder.bind(orderExpire).to(commonDelayExchange).with(Constant.ROUTING_KEY_ORDER_EXPIRE).noargs();
    }

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate) {
        return new AsyncRabbitTemplate(rabbitTemplate);
    }

    @Bean
    public RabbitListenerErrorHandler commonErrorHandler(RabbitTemplate rabbitTemplate) {
        return (amqpMessage, message, exception) -> {
            logger.error("处理MQ消息时错误: " + exception.getFailedMessage(), exception);
            final Object payload = message.getPayload();
            if (payload instanceof MQRetryMsg) {
                final MQRetryMsg retryMsg = (MQRetryMsg) payload;
                int retryCount = retryMsg.getRetry();

                int delay;
                // 重试间隔依次增大 5s 1min 5min 30min 1h
                switch (retryCount) {
                    default:
                    case 0:
                        delay = 5000;
                        break;
                    case 1:
                        delay = 60 * 1000;
                        break;
                    case 2:
                        delay = 5 * 60 * 1000;
                        break;
                    case 3:
                        delay = 30 * 60 * 1000;
                        break;
                    case 4:
                        delay = 60 * 60 * 1000;
                        break;
                }
                if (retryMsg.getRetry() >= retryMsg.getMaxRetry()) {
                    logger.error("消息处理达到最大重试次数，丢弃消息: {}", JSON.toJSONString(message.getPayload()));
                    final MessageProperties messageProperties = amqpMessage.getMessageProperties();

                    new MQFailLog(MQFailType.MAX_RETRY_REACHED, messageProperties.getReceivedExchange(),
                            messageProperties.getReceivedRoutingKey(), message.getPayload()).insert();
                    // 此异常则强制让rabbitmq丢弃此条处理失败消息，不放回queue
                    throw new AmqpRejectAndDontRequeueException("max retry times reached");
                } else {
                    logger.error("处理消息时错误（这是第{}次重试），将于{}秒后再次重试", retryCount, delay / 1000);
                    final int de = delay;
                    retryMsg.setRetry(retryMsg.getRetry() + 1);
                    rabbitTemplate.convertAndSend(COMMON_DELAY_EXCHANGE,
                            amqpMessage.getMessageProperties().getReceivedRoutingKey(),
                            retryMsg, messageToSent -> {
                                MessageProperties messageProperties = messageToSent.getMessageProperties();
                                messageProperties.setDelay(de);
                                return messageToSent;
                            });
                }
            } else {
                final MessageProperties messageProperties = amqpMessage.getMessageProperties();
                new MQFailLog(MQFailType.ERROR_HANDLING, messageProperties.getReceivedExchange(),
                        messageProperties.getReceivedRoutingKey(), message.getPayload()).insert();
            }
            return null;
        };
    }
}
