package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * 校验支付密码类型
 */
public enum CheckPayType implements IEnum<Integer> {

    /**
     * 绑定银行卡
     */
    @JsonProperty("1")
    BIND_BANK_CODE,

    /**
     * 提现
     */
    @JsonProperty("2")
    WITHDRAW,

    /**
     * 解绑银行卡
     */
    @JsonProperty("3")
    CANCEL_BANK_CODE,

    /**
     * 解绑银行卡
     */
    @JsonProperty("4")
    CANCEL_BANK_CODE_2
    ;

    public final int value = this.ordinal()+1;

    public static CheckPayType thisValueOf(Integer type) {
        if (type == null) {
            return null;
        } else {
            for (CheckPayType s : CheckPayType.values()) {
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
