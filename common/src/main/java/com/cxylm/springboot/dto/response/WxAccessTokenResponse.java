package com.cxylm.springboot.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WxAccessTokenResponse extends WxBaseResponse {
    String access_token;
    String expires_in;
}
