package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: shiyanru
 * @Date: 2020/3/17 11:29
 */

public enum AccountType implements IEnum<Integer> {
    /**
     * 学生/家长（同一账号）
     */
    STUDENT,

    /**
     * 校长
     */
    MASTER;

    @JsonValue
    private final int value = this.ordinal();

    @Override
    public Integer getValue() {
        return value;
    }
}
