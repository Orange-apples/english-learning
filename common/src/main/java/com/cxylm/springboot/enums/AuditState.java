package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * 审核状态
 */
public enum AuditState implements IEnum {
    /**
     * 默认状态：待审核
     */
    DEFAULT,

    /**
     * 审核失败
     */
    REJECT,

    /**
     * 审核通过
     */
    PASS;

    public final int mask = this.ordinal();

    public static AuditState thisValueOf(Integer type) {
        if (type == null) {
            return null;
        } else {
            for (AuditState s : AuditState.values()) {
                if (s.getValue() == type) {
                    return s;
                }
            }
            return null;
        }
    }

    public static int of(AuditState[] features) {
        int res = 0;
        if (features != null) {
            for (AuditState feature : features) {
                res |= feature.mask;
            }
            return res;
        }
        return res;
    }


    @Override
    public Serializable getValue() {
        return this.mask;
    }

    @JsonValue
    public int value() {
        return this.mask;
    }

}
