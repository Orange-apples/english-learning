package com.cxylm.springboot.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WXOAuthResponse extends WxBaseResponse {
    /**
     * 发送回执ID
     **/
    String access_token;
    /**
     * 请求状态码
     **/
    String expires_in;
    /**
     * 状态码的描述
     */
    String refresh_token;
    /**
     * 请求ID
     */
    String openid;
    String scope;
}
