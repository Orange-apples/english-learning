package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 学习类型
 */
public enum TestType implements IEnum<Integer> {
    //1汉译英，2英译汉，3听选，4听写，5单元测试，6一测到底，7学前测试，8学后测试
    NO,
    CH_TO_EN,
    EN_TO_CH,
    LISTEN_TO_CH,
    LISTEN_TO_WRITE,
    UNIT_TEST,
    UNTIL_TEST,
    BEFORE_TEST,
    AFTER_TEST
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
