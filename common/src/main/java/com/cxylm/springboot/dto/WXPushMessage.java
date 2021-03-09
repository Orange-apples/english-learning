package com.cxylm.springboot.dto;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class WXPushMessage implements Serializable {
    public String openId;
    public String text;
}
