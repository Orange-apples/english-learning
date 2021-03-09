package com.cxylm.springboot.constant.config;

import lombok.Getter;

/**
 * @author: HaoTi
 * @date: 19/2/10
 * @description:
 */
@Getter
public class AlipayConfig {

//    @Autowired
//    private AliPayProperties aliPayProperties;

    /** 商户appid**/
//    private String app_id= aliPayProperties.getAliAppId();
    /** 私钥 pkcs8格式的**/
//    private String rsa_private_key=aliPayProperties.getAliPriKey();
    /** 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问**/
//    private String notify_url=MchInfoConfig.getConfig().getAliNotifyUrl();
    private String return_url;
    /** 请求网关地址**/
    public static String url = "https://openapi.alipay.com/gateway.do";
    /** 编码**/
    public static String CHARSET = "UTF-8";
    /** 返回格式**/
    public static String FORMAT = "json";
    /** 支付宝公钥**/
//    public String alipay_public_key=aliPayProperties.getAliPubKey();
    /** RSA2**/
    public static String SIGNTYPE = "RSA2";

    public static String ALIPAY_URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";
}

