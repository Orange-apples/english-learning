package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BookLevel implements IEnum<Integer> {
    //阶段（0、异常，1、小学英语，2、初中英语，3、高中英语，4、大学英语，5、托福/雅思）
    ERROR,

    XIAO_XUE,
    CHU_ZHONG,
    GAO_ZHONG,
    DA_XUE,
    TOEFL
    ;

    private final int value = this.ordinal();

    public static BookLevel valueOfInt(int value) {
        for (BookLevel type : BookLevel.values()) {
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
