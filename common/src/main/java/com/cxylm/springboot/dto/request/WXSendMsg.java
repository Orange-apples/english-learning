package com.cxylm.springboot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class WXSendMsg implements Serializable {
    private String touser;
    private String msgtype;
}
