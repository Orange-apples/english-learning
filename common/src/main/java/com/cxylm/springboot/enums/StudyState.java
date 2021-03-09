package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StudyState implements IEnum<Integer> {
    //状态，0、未开始，1、学习中，2、已完成
    CREATE,
    LEARNING,
    OVER
    ;

    private final int value = this.ordinal();

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StudyState valueOfInt(int value) {
        for (StudyState type : StudyState.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
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
