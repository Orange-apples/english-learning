package com.cxylm.springboot.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.cxylm.springboot.constant.config.AlipayConfig;
import com.cxylm.springboot.properties.AliPayProperties;
import com.cxylm.springboot.config.property.AppProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliSDKConfig {
    @Bean(destroyMethod = "shutdown")
    public IAcsClient iAcsClient(AppProperty appProperty) {
        DefaultProfile profile = DefaultProfile.getProfile("default",
                appProperty.getAliAccessKey(),
                appProperty.getAliSecret());
        return new DefaultAcsClient(profile);
    }

//    @Bean
    public DefaultAlipayClient defaultAlipayClient(AliPayProperties aliPayProperties) throws AlipayApiException {
        return new DefaultAlipayClient(getCertAlipayRequest(aliPayProperties));
    }

    private CertAlipayRequest getCertAlipayRequest(AliPayProperties aliPayProperties) {
        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
        //设置应用Id
        certAlipayRequest.setAppId(aliPayProperties.getAliAppId());
        //设置应用私钥
        certAlipayRequest.setPrivateKey(aliPayProperties.getAliPriKey());
        //设置请求格式，固定值json
        certAlipayRequest.setFormat("json");
        //设置字符集
        certAlipayRequest.setCharset(AlipayConfig.CHARSET);
        //设置签名类型
        certAlipayRequest.setSignType(AlipayConfig.SIGNTYPE);
        //设置应用公钥证书路径
        certAlipayRequest.setCertPath(aliPayProperties.getAliCertPath());
        //设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(aliPayProperties.getAlipayPublicCertPath());
        //设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(aliPayProperties.getAliRootCertPath());
        return certAlipayRequest;
    }
}
