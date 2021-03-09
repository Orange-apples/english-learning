package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PayOrderState implements IEnum<Integer> {
    /**
     * 初始化
     */
    INIT,

    /**
     * 支付中
     */
    PAYING,

    /**
     * 支付成功
     */
    SUCCESS,

    /**
     * 支付失败
     */
    FAILED,

    /**
     * 交易完成
     */
    CLOSE,

    ;

    @JsonValue
    private final int value = this.ordinal();

    @Override
    public Integer getValue() {
        return value;
    }
}
