package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.annotation.validator.MobilePhone;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 账户设置from
 */
@Data
public class PasswordResetForm {
    @NotNull(message = "请输入手机号")
    @MobilePhone
    private String mobile;

    /**
     * 新密码
     */
    @NotBlank(message = "请输入新密码")
    @Length(min = 6, max = 32, message = "密码最少6位，最多32位")
    private String password;

    @NotBlank(message = "请输入短信验证码")
    private String smsCode;
}
