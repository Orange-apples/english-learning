package com.cxylm.springboot.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AliSmsResponse {
    /**发送回执ID**/
    String BizId;
    /**请求状态码**/
    String Code;
    /**状态码的描述*/
    String Message;
    /**请求ID*/
    String RequestId;
}
