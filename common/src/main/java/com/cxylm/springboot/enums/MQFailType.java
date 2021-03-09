package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;


public enum MQFailType implements IEnum<Integer> {

    /**
     * 发送失败
     */
    SEND_FAILED,

    /**
     * 达到最大重试次数
     */
    MAX_RETRY_REACHED,

    /**
     * 处理时出错(不重试)
     */
    ERROR_HANDLING,

    ;

    @JsonValue
    private final int value = this.ordinal();

    public static MQFailType ofValue(Integer type) {
        if (type == null) {
            return null;
        } else {
            for (MQFailType s : MQFailType.values()) {
                if (s.value == type) {
                    return s;
                }
            }
            return null;
        }
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
