package com.cxylm.springboot.enums;

import lombok.Getter;

/**
 * @author tang
 * @date 2021-03-16 11:00
 */
@Getter
public enum SchoolEnum {
    SCHOOL_ENABLE_0(0, "禁用"),
    SCHOOL_ENABLE_1(1, "启用");


    SchoolEnum(Integer value, String desc) {
        this.value = value;
        this.des = desc;
    }

    private Integer value;
    private String des;
}
