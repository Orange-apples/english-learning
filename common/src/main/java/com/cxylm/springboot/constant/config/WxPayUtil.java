package com.cxylm.springboot.constant.config;

import com.cxylm.springboot.enums.ChannelEnum;
import com.cxylm.springboot.properties.PayParams;
import com.cxylm.springboot.properties.WxPayProperties;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.cxylm.springboot.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: HaoTi
 * @date: 19/9/10
 */
@Component
public class WxPayUtil {

    @Autowired
    private static WxPayProperties wxPayProperties = null;

    @Autowired
    public WxPayUtil(WxPayProperties wxPayProperties) {
        WxPayUtil.wxPayProperties = wxPayProperties;
    }
    /**
     * 获取微信支付配置
     *
     * @param configParam
     * @param tradeType
     * @param notifyUrl
     * @return
     */
    public static WxPayConfig getWxWithdrawConfig(PayParams configParam, String tradeType, String notifyUrl) {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(configParam.getMchId());
        wxPayConfig.setAppId(configParam.getAppId());
//        wxPayConfig.setKeyPath(certRootPath + File.separator + paramObj.getString("certLocalPath"));
//        wxPayConfig.setKeyPath(WxPayUtil.class.getClassLoader().getResource("").getPath().substring(1) + "apiclient_cert.p12");
//        wxPayConfig.setKeyPath("C:\\Users\\Administrator\\Desktop\\cert\\apiclient_cert.p12");
        wxPayConfig.setKeyPath(wxPayProperties.getWxCertRootPath());
        wxPayConfig.setMchKey(configParam.getKey());
        wxPayConfig.setNotifyUrl(notifyUrl);
        wxPayConfig.setTradeType(tradeType);
        return wxPayConfig;
    }

    /**
     * 获取微信支付配置
     *
     * @return
     * @param channel
     */
    public static WxPayConfig getWxPayConfigByNotify(ChannelEnum channel) {
//        PayParams configParam = wxPayProperties.getWxPayParams();
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(wxPayProperties.getWxMchId());
        wxPayConfig.setMchKey(wxPayProperties.getWxKey());
        if(ChannelEnum.WX_JSAPI.equals(channel)){
            wxPayConfig.setAppId(wxPayProperties.getWxJsAppId());
        }else{
            wxPayConfig.setAppId(wxPayProperties.getWxAppId());
        }
        return wxPayConfig;
    }

    /**
     * 下单的微信配置
     *
     * @return
     * @param channel
     */
    public static WxPayConfig getWxPayConfig(ChannelEnum channel) {
        //wxPayProperties.getWxPayParams(), PayConstant.WxConstant.TRADE_TYPE_APP, null, wxPayProperties.getWxNotifyUrl()
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(wxPayProperties.getWxMchId());
        wxPayConfig.setKeyPath(WxPayUtil.class.getClassLoader().getResource("").getPath().substring(1) + "apiclient_cert.p12");
        wxPayConfig.setMchKey(wxPayProperties.getWxKey());
        wxPayConfig.setNotifyUrl(wxPayProperties.getWxNotifyUrl());
        switch (channel) {
            case WX_APP: {
                wxPayConfig.setAppId(wxPayProperties.getWxAppId());
                wxPayConfig.setTradeType(Constant.TRADE_TYPE_APP);
                break;
            }
            case WX_JSAPI: {
                wxPayConfig.setAppId(wxPayProperties.getWxJsAppId());
                wxPayConfig.setTradeType(Constant.TRADE_TYPE_JSPAI);
                break;
            }
            default:
        }
        return wxPayConfig;
    }
}
