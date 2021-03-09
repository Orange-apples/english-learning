package com.cxylm.springboot.service.mq;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.cxylm.springboot.constant.Constant;
import com.cxylm.springboot.dto.SMSMessage;
import com.cxylm.springboot.dto.WXPushMessage;
import com.cxylm.springboot.dto.response.AliSmsResponse;
import com.cxylm.springboot.service.push.MessageSender;
import com.cxylm.springboot.service.push.PushMessage;
import com.cxylm.springboot.util.WXUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import static com.cxylm.springboot.config.QueueConfig.QUEUE_SMS;
import static com.cxylm.springboot.config.QueueConfig.QUEUE_WX_PUSH;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MQConsumer {
    private static final String COMMON_ERROR_HANDLER = "commonErrorHandler";

    private final IAcsClient iAcsClient;
    //    private final JPushClient jPushClient;
    private JPushClient jPushClient;
    private final Gson gson;
    private final WXUtil wxUtil;

    private static final Platform PUSH_PLATFORM = Platform.android_ios();

    /**
     * 发送短信
     */
    @RabbitListener(queues = QUEUE_SMS, errorHandler = COMMON_ERROR_HANDLER)
    public void onSMSMQMsgReceived(@Payload SMSMessage msg) {
        // 发送短信
        sendSMS(msg.phone, msg.template, msg.params);
    }

    @RabbitListener(queuesToDeclare = @Queue(value = QUEUE_WX_PUSH), errorHandler = COMMON_ERROR_HANDLER)
    public void sendWXPush(@Payload WXPushMessage msg) {
        log.info("微信推送:  {}", msg.toString());
        // 发送微信推送
        wxUtil.sendTextMsgToSpecificUser(msg.openId, msg.text);
    }

    /**
     * 30分钟后删除未支付订单
     */
    @RabbitListener(queues = Constant.QUEUE_ORDER_EXPIRE, errorHandler = COMMON_ERROR_HANDLER)
    public void orderExpire(@Payload PayDelayMessage payDelay) {
        log.info("Sending payDelay for {}, content: {}", payDelay.id, payDelay.type);
        // 删除过期订单
        switch (payDelay.type) {
            case PAY:
//                payService.updateOrderExpire(payDelay.id);
                break;
            default:
        }
    }

    @RabbitListener(queuesToDeclare = @Queue(value = Constant.QUEUE_PUSH), errorHandler = COMMON_ERROR_HANDLER)
    public void sendPush(@Payload MessageSender sender) {
        final PushMessage message = sender.getMessage();
        log.info("发送推送消息:  {}", JSON.toJSONString(message));

        final PushPayload payload = getPushPayload(sender);
        try {
            if (payload == null) {
                return;
            }
            PushResult result = jPushClient.sendPush(payload);
            log.info("推送结果： " + result);
            // 如果使用 NettyHttpClient，需要手动调用 close 方法退出进程
            // If uses NettyHttpClient, call close when finished sending request, otherwise process will not exit.
            // jpushClient.close();
        } catch (APIConnectionException e) {
            log.error("极光推送网络异常", e);
            log.error("异常推送编码: " + payload.getSendno());

        } catch (APIRequestException e) {
            log.error("极光推送请求异常 {}", e.getMessage());
            log.error("HTTP Status: {}, Error Code: {}, Error Message: {}, MsgId: {}, Send no: {}",
                    e.getStatus(), e.getErrorCode(), e.getErrorMessage(), e.getMsgId(), payload.getSendno());
        }
    }

    private void sendSMS(String mobile, String template, Object params) {
        CommonRequest request = new CommonRequest();
        final String paramString = JSON.toJSONString(params);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "default");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "BTC英语");
        request.putQueryParameter("TemplateCode", template);
        if (params != null) {
            request.putQueryParameter("TemplateParam", paramString);
        }

        try {
            log.info("Sending SMS for {}, template:{}, params: {}",
                    mobile, template, paramString);
            CommonResponse response = iAcsClient.getCommonResponse(request);
            AliSmsResponse aliSmsDto = JSON.parseObject(response.getData(), AliSmsResponse.class);
            if (!"OK".equals(aliSmsDto.getCode())) {
                log.error("短信发送失败！ {}", aliSmsDto.toString());
            }
        } catch (ClientException e) {
            log.error("短信发送失败！", e);
        }
    }


    private PushPayload getPushPayload(MessageSender sender) {
        final PushMessage pushMessage = sender.getMessage();

        final PushPayload.Builder payloadBuilder = PushPayload.newBuilder()
                .setPlatform(PUSH_PLATFORM);

        if (!StringUtils.isEmpty(sender.getGroupTag())) {
            // GroupTag不为空说明是群组推送
            payloadBuilder.setAudience(Audience.tag(sender.getGroupTag()));
        } else if (CollectionUtils.isEmpty(sender.getPushDeviceList())) {
            // DeviceList为空，说明是全体推送
            payloadBuilder.setAudience(Audience.all());
        } else {
            // 否则就是指定用户推
            payloadBuilder.setAudience(Audience.alias(sender.getPushDeviceList()));
        }

        final AndroidNotification.Builder androidBuilder = AndroidNotification.newBuilder();
        final IosNotification.Builder iosBuilder = IosNotification.newBuilder().incrBadge(0);

        if (pushMessage.getSound() != null) {
            androidBuilder.setSound(pushMessage.getSound());
            iosBuilder.setSound(pushMessage.getSound() + ".mp3");
        }

        if (sender.isTransparentPush()) {
            final JsonElement jsonElement = gson.toJsonTree(pushMessage.getPayload());
            JsonObject jsonObject = null;
            if (jsonElement.isJsonObject()) {
                jsonObject = jsonElement.getAsJsonObject();
            } else {
                log.warn("PUSH WARNING: Push payload is not a json object, ignore payload");
                pushMessage.setContent("");
            }

            androidBuilder.addExtra("content", pushMessage.getContent());
            iosBuilder.addExtra("type", pushMessage.getPushType().getValue());

            if (jsonObject != null) {
                androidBuilder.addExtra("payload", jsonObject);
                iosBuilder.addExtra("payload", jsonObject);
            }

            if (pushMessage.isNeedNotify()) {
                androidBuilder.setAlert(pushMessage.getContent());
                androidBuilder.setTitle(pushMessage.getTitle());
                iosBuilder.setAlert(pushMessage.getContent());
            } else {
                androidBuilder.addExtra("type", pushMessage.getPushType().getValue());
                androidBuilder.addExtra("title", pushMessage.getTitle());
                androidBuilder.setAlert("");
                iosBuilder.setAlert("");
            }
        } else {
            androidBuilder.setAlert(pushMessage.getContent());
            androidBuilder.setTitle(pushMessage.getTitle());
            iosBuilder.setAlert(pushMessage.getContent());
        }

        payloadBuilder.setNotification(
                Notification.newBuilder()
                        //.setAlert(pushMessage.getContent())
                        .addPlatformNotification(androidBuilder.build())
                        .addPlatformNotification(iosBuilder.build())
                        .build());

        final Options options = Options.newBuilder().setApnsProduction(true).setTimeToLive(pushMessage.getTtl()).build();
        return payloadBuilder.setOptions(options).build();
    }
}
