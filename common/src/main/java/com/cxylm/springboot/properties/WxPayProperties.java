package com.cxylm.springboot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cxylm.wx-pay")
public class WxPayProperties {
    /** 用户版支付配置 */
    private String wxMchId;
    private String wxAppId;
    private String wxJsAppId;
    private String wxKey;
    private String wxAppSecret;
    private String wxJsAppSecret;
//    private PayParams wxPayParams;
    private PayParams wxWithdrawParams;
    private String wxCertRootPath;
    private String wxNotifyUrl;
    private String wxDepositNotifyUrl;
    private String wxRefundNotifyUrl;

}
