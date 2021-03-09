package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 记忆等级
 */
public enum MemoryLevel implements IEnum<Integer> {
    //0、一天以内，1、一天，2、两天，3、四天，4、七天，5、十五天
    HOUR,
    ONE_DAY,
    TWO_DAY,
    FOUR_DAY,
    WEEK,
    FIFTEEN_DAY
    ;

    private final int value = this.ordinal();

    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonValue
    public int value() {
        return this.value;
    }
}
