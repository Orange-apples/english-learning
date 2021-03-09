package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountState implements IEnum<Integer> {
    /**
     * 正常状态
     */
    NORMAL,

    /**
     * 账号被锁定
     */
    LOCKED,

    /**
     * 账号注销
     */
    LOGOUT,

    ;

    @JsonValue
    private final int value = this.ordinal();

    @Override
    public Integer getValue() {
        return value;
    }
}
