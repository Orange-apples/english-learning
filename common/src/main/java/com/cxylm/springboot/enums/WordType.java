package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 学习类型
 */
public enum WordType implements IEnum<Integer> {
    //0生词，1一般词,2熟词
    NEW_WORDS,
    GENERAL_WORDS,
    FAMILIAR_WORDS
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
