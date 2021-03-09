package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.annotation.validator.MobilePhone;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 短信验证
 */
@Getter
@Setter
public class SMSCodeForm {
    /**
     * 手机号
     */
    @NotNull
    @Length(max = 16, message = "手机号长度不能超过16个字符")
    @MobilePhone(message = "手机号格式不正确")
    private String mobile;

    @NotNull(message = "缺少验证码类型参数")
    private Integer type;
}
