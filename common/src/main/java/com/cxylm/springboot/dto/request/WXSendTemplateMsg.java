package com.cxylm.springboot.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WXSendTemplateMsg extends WXSendMsg {
    private Data data;
    private String template_id;
    private String url = "https://btcyingyu.com/parents/";
    private String topcolor = "#FF0000";

    public WXSendTemplateMsg(String openid) {
        super(openid, "text");
    }

    @Getter
    @Setter
    public static class Data {
        private String value;
        private String color;
    }
}
