package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PushType implements IEnum<Integer> {

    /**
     * 商家收款通知
     */
    NEW_PAYMENT(0),

    SYSTEM_NOTICE(10),

    /**
     * 已被邀请成为会员通知
     */
    INVITE_NOTICE(11),

    /**
     * 收益到账
     */
    INCOME_NOTICE(20),

    ;

    @JsonValue
    private final int value;

    PushType(final int value) {
        this.value = value;
    }

    @JsonCreator
    public static PushType ofInt(int value) {
        for (PushType type : PushType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}

