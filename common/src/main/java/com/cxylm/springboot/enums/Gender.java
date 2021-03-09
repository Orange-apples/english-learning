package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender implements IEnum<Integer> {
    MAN,

    WOMAN,

    SECRET
    ;

    @JsonValue
    private final int value = this.ordinal();

    @Override
    public Integer getValue() {
        return value;
    }
}
