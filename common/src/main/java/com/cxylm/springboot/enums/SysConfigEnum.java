package com.cxylm.springboot.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @auther Orange-apples
 * @date 2021/3/15 22:24
 */
@Getter
public enum SysConfigEnum {
    PUSH_ENABLE_0("0", "推送关闭"),
    PUSH_ENABLE_1("1", "推送开启");


    SysConfigEnum(String value, String desc) {
        this.value = value;
        this.des = desc;
    }

    private String value;
    private String des;

}
