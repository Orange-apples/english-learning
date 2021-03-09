package com.cxylm.springboot.dto;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class SMSMessage implements Serializable {
    private static final long serialVersionUID = -588066724264743561L;
    public String phone;
    public String template;
    public Object params;
}
