package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.annotation.validator.MobilePhone;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserRegisterForm {
    @NotBlank(message = "手机号不能为空")
    @MobilePhone(message = "手机号格式不正确")
    private String mobile;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    //@NotBlank(message = "验证码不正确")
    private String smsCode;

    private Integer bdCode;

    private Long expire;
}
