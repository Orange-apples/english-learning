package com.cxylm.springboot.service.push;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter @Setter
public class MessageSender implements Serializable {
    private static final long serialVersionUID = -2288122455602261521L;
    /**
     * 是否为透传推送
     */
    private boolean isTransparentPush = false;

    /**
     * 是否群组推送
     */
    private boolean isBroadcastPush = false;

    /**
     * 群组名
     */
    private String groupTag;

    /**
     * 推送设备列表
     */
    private List<String> pushDeviceList;

    /**
     * 发送的消息
     */
    private PushMessage message;
}
