package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 单词本类型
 */
public enum WordBookType implements IEnum<Integer> {
    //0字母顺序，1熟悉程度，2生词，3一般词，4熟词
    A_Z,
    FAMILIAR_ORDER,
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
