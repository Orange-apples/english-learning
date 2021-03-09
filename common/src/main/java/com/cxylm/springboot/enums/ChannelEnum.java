package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * @Description: 支付方式定义
 * @author HaoTi
 * @date 2019-01-25
 * @version V1.0
 */
public enum ChannelEnum implements IEnum<Integer> {
    /**平台发放*/
    SYSTEMPAY,

    /**微信APP支付*/
    WX_APP,

    /**支付宝支付*/
    ALIPAY,

    /**余额支付*/
    BALANCE,

    /**银联支付*/
    UNIONPAY,

    /**微信小程序支付*/
    WX_JSAPI,

    /**建设银行*/
    CONSTRUCTION,

    /**邮储银行*/
    POSTALSAVINGS,

    /**工商银行*/
    INDUSTRIALANDCOMMERCIAL,

    /**农业银行*/
    AGRICULTURAL,

    /**中国银行*/
    CHINABANK,

    /**交通银行*/
    COMMUNICATIONS,

    /**中信银行*/
    CITIC,

    /**民生银行*/
    MINSHENG,

    /**招商银行*/
    MERCHANTS,

    /**平安银行*/
    PINGBANK;

    private final int value = this.ordinal();

    public static ChannelEnum thisValueOf(Integer type) {
        if (type == null) {
            return null;
        } else {
            for (ChannelEnum s : ChannelEnum.values()) {
                if (s.getValue().equals(type)) {
                    return s;
                }
            }
            return null;
        }
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonValue
    public int value() {
        return this.value;
    }
}
