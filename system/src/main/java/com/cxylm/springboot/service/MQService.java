package com.cxylm.springboot.service;

import com.alibaba.fastjson.JSON;
import com.cxylm.springboot.enums.MQFailType;
import com.cxylm.springboot.model.MQFailLog;
//import com.cxylm.springboot.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.Serializable;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MQService {
    private final RabbitTemplate rabbitTemplate;
    private final AsyncRabbitTemplate asyncRabbitTemplate;
//    private final LogService logService;

    /**
     * 发送消息至MQ
     *
     * @param routingKey 队列名或交换机名
     * @param payload    消息体
     */
    public void sendMsgToMQ(final String routingKey, final Serializable payload) {
        try {
            rabbitTemplate.convertAndSend(routingKey, payload);
        } catch (Exception e) {
            log.error("发送MQ消息失败，routingKey:{}, payload:{}", routingKey, JSON.toJSONString(payload));
            log.error("发送MQ消息异常", e);
            insertFailLog(MQFailType.SEND_FAILED, null, routingKey, payload);
        }
    }

    /**
     * 发送消息至MQ
     *
     * @param exchangeName 队列名或交换机名
     * @param routingKey   路由键
     * @param payload      消息体
     */
    public void sendMsgToMQ(final String exchangeName, final String routingKey, final Serializable payload) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, payload);
        } catch (Exception e) {
            log.error("发送MQ消息失败，exchangeName:{}, routingKey:{}, payload:{}",
                    exchangeName, routingKey, JSON.toJSONString(payload));
            log.error("发送MQ消息异常", e);
            insertFailLog(MQFailType.SEND_FAILED, exchangeName, routingKey, payload);
        }
    }

    /**
     * 发送延迟消息至MQ
     *
     * @param exchangeName 交换机名(<span style="color:red">必须是x-delayed-message类型的exchange</span>)
     * @param routingKey   路由键
     * @param payload      消息体
     */
    public void sendDelayedMsgToMQ(final String exchangeName, final String routingKey,
                                   final Serializable payload, final int delay) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, payload, message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setDelay(delay);
                return message;
            });
        } catch (Exception e) {
            log.error("发送延迟MQ消息失败，exchangeName:{}, routingKey:{}, delay:{}ms, payload:{}",
                    exchangeName, routingKey, delay, JSON.toJSONString(payload));
            log.error("发送延迟MQ消息异常", e);
            insertFailLog(MQFailType.SEND_FAILED, exchangeName, routingKey, payload);
        }
    }

    /**
     * 发送消息至MQ
     *
     * @param routingKey 队列名或交换机名
     * @param payload    消息体
     */
    public <T> AsyncRabbitTemplate.RabbitConverterFuture<T> sendAndReceiveAsync(
            final String routingKey, final Serializable payload) {
        final AsyncRabbitTemplate.RabbitConverterFuture<T> resFuture =
                asyncRabbitTemplate.convertSendAndReceive(routingKey, payload);
        resFuture.addCallback(new ListenableFutureCallback<T>() {

            @Override
            public void onSuccess(T t) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("异步发送MQ消息失败，routingKey:{}, payload:{}", routingKey, JSON.toJSONString(payload));
                log.error("异步发送MQ消息异常", throwable);
                insertFailLog(MQFailType.SEND_FAILED, null, routingKey, payload);
            }
        });
        return resFuture;
    }

    /**
     * 发送消息至MQ
     *
     * @param exchangeName 队列名或交换机名
     * @param routingKey   路由键
     * @param payload      消息体
     */
    public <T> AsyncRabbitTemplate.RabbitConverterFuture<T> sendAndReceiveAsync(
            final String exchangeName, final String routingKey, final Serializable payload) {
        final AsyncRabbitTemplate.RabbitConverterFuture<T> resFuture =
                asyncRabbitTemplate.convertSendAndReceive(exchangeName, routingKey, payload);

        resFuture.addCallback(new ListenableFutureCallback<T>() {

            @Override
            public void onSuccess(T t) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("异步发送MQ消息失败，exchangeName:{}, routingKey:{}, payload:{}",
                        exchangeName, routingKey, JSON.toJSONString(payload));
                log.error("异步发送MQ消息异常", throwable);
                insertFailLog(MQFailType.SEND_FAILED, exchangeName, routingKey, payload);
            }
        });
        return resFuture;
    }

    private void insertFailLog(MQFailType failType, String exchangeName, String routingKey, Serializable payload) {
//        logService.insertMQFailLog(new MQFailLog(failType, exchangeName, routingKey, payload));
    }
}
