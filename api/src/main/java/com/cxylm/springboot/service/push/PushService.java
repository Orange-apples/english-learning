package com.cxylm.springboot.service.push;

import com.cxylm.springboot.service.mq.MQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.cxylm.springboot.constant.Constant.QUEUE_PUSH;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PushService {
    private final MQService mqService;

    /**
     * 发送标题推送至单一设备
     */
    public void sendPush(Integer userId, PushMessage pushMessage) {
        final List<String> userIds = Collections.singletonList(userId.toString());
        sendPush(userIds, pushMessage, false);
    }

    /**
     * 发送透传推送至单一设备
     */
    public void sendTransPush(Integer userId, PushMessage pushMessage) {
        final List<String> userIds = Collections.singletonList(userId.toString());
        sendPush(userIds, pushMessage, true);
    }

    /**
     * 发送推送至多设备
     */
    public void sendMultiPush(List<String> userIds, PushMessage pushMessage) {
        sendPush(userIds, pushMessage, false);
    }

    /**
     * 发送透传推送至多设备
     */
    public void sendMultiTransPush(List<String> userIds, PushMessage pushMessage) {
        sendPush(userIds, pushMessage, true);
    }

    /**
     * 群发
     */
    public void sendToGroup(String group, PushMessage pushMessage) {
        sendPushToGroup(group, pushMessage, false);
    }

    /**
     * 群发透传
     */
    public void sendTransPushToGroup(String group, PushMessage pushMessage) {
        sendPushToGroup(group, pushMessage, true);
    }

    private void sendPushToGroup(String tag, PushMessage pushMessage, boolean isTransparent) {
        MessageSender sender = new MessageSender();
        // set sender data
        sender.setTransparentPush(isTransparent);
        sender.setBroadcastPush(true);
        sender.setGroupTag(tag);
        sender.setMessage(pushMessage);
        mqService.sendMsgToMQ(QUEUE_PUSH, sender);
    }

    private void sendPush(List<String> userIds, PushMessage pushMessage, boolean isTransparent) {
        MessageSender sender = new MessageSender();
        sender.setTransparentPush(isTransparent);
        sender.setMessage(pushMessage);
        sender.setPushDeviceList(userIds);
        mqService.sendMsgToMQ(QUEUE_PUSH, sender);
    }
}
