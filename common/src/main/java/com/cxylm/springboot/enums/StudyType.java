package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 学习类型
 */
public enum StudyType implements IEnum<Integer> {
    //0、复习，1、单词学习，2、智能听写，3、听读训练（默认为0复习）
    REVIEW,
    LEARN,
    DICTATION,
    LISTEN_TEST
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
