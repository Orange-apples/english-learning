package com.cxylm.springboot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cxylm.ali-pay")
public class AliPayProperties {
    /** 用户版支付配置 */
    private String aliAppId;
    private String aliPubKey;
    private String aliPriKey;
//    private String aliWithdrawAppId;
//    private String aliWithdrawPubKey;
//    private String aliWithdrawPriKey;
    private String aliNotifyUrl;
    private String aliDepositNotifyUrl;
    private String aliRefundNotifyUrl;
    private String aliCertPath;
    private String alipayPublicCertPath;
    private String aliRootCertPath;

//    /** 商家版支付配置 */
//    private String mchAliAppId;
//    private String mchAliPubKey;
//    private String mchAliPriKey;
//    private String mchAliNotifyUrl;
//    private String mchAliDepositNotifyUrl;
//    private String mchAliRefundNotifyUrl;
}
