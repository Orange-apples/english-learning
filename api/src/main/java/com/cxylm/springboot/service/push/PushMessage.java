package com.cxylm.springboot.service.push;

import com.cxylm.springboot.enums.PushType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class PushMessage implements Serializable {
    private static final long serialVersionUID = 3609715686317575547L;

    private String user;

    private List<String> pushAliasList;

    /**
     * 通知栏展示的通知的标题
     */
    private String title;

    //private boolean isTransparent;

    /**
     * 通知栏展示的通知描述
     */
    private String content;

    private PushType pushType;

    private String tts;
    private Object payload;
    private String sound;
    private boolean needNotify;
    private long ttl;

    private PushMessage(Builder builder) {
        this.user = builder.user;
        this.title = builder.title;
        this.content = builder.content;
        this.pushType = builder.pushType;
        this.tts = builder.tts;
        this.payload = builder.payload;
        this.sound = builder.sound;
        this.needNotify = builder.needNotify;
        this.ttl = builder.ttl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String user;
        private String title;
        private String content = "";
        private String tts;
        private PushType pushType;
        private Object payload;
        private String sound;
        private boolean needNotify;
        // 默认推送消息生存时间为60秒
        private long ttl = 60L;
        private long channel = 60L;

        Builder() {
        }

        public Builder user(Integer user) {
            this.user = user.toString();
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        /**
         * Text to speech
         *
         * @param tts tts文字
         */
        public Builder tts(String tts) {
            this.tts = tts;
            return this;
        }

        /**
         * 离线消息保留时长(秒)
         * @param seconds 时长(秒)
         */
        public Builder ttlSeconds(long seconds) {
            this.ttl = seconds;
            return this;
        }

        public Builder pushType(PushType type) {
            this.pushType = type;
            return this;
        }

        public Builder payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public Builder sound(String sound) {
            this.sound = sound;
            return this;
        }

        public Builder needNotify(boolean needNotify) {
            this.needNotify = needNotify;
            return this;
        }

        public PushMessage build() {
            return new PushMessage(this);
        }
    }
}
