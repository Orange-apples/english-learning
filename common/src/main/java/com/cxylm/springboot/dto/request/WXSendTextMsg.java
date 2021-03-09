package com.cxylm.springboot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WXSendTextMsg extends WXSendMsg {
    private Text text;

    public WXSendTextMsg(String openid, String textMsg) {
        super(openid, "text");
        this.text = new Text(textMsg);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Text {
        private String content;
    }
}
