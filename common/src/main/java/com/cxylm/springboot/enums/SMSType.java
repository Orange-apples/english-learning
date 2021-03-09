package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum SMSType  implements IEnum<Integer> {
    /**
     * 1、用户登录验证码
     */
    SMS_CODE(1),
    /**2、注册账号*/
//    REGISTER,
    /**3、修改密码*/
    UPDATE_PWD(3),
    /**4、重置密码*/
    RESET_PWD(4),
    /**5、更换手机号-->新手机*/
//    UPDATE_PHONE,
    /**6、随机码账户登录*/
    LOGIN_SMS_CODE(6),
    /**7、设置支付密码*/
    SET_PAY_PWD(7),
    /**8、找回支付密码*/
    RESET_PAY_PWD(8),
    /**9、绑定银行卡*/
    BIND_BANK_CARD(9),
    /**10、注销*/
    LOGOUT(10);

    private int value;

    SMSType(final int value) {
        this.value = value;
    }

    @JsonCreator
    public static SMSType valueOfInt(int value) {
        for (SMSType type : SMSType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public Integer getValue() {
        return this.value;
    }
}

