package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.validator.group.PasswordLoginGroup;
import com.cxylm.springboot.validator.group.SMSLoginGroup;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginForm {
    @NotBlank(groups = PasswordLoginGroup.class, message = "用户名不能为空")
    private String username;
    @NotBlank(groups = PasswordLoginGroup.class)
    private String password;
    @NotBlank(groups = SMSLoginGroup.class)
    private String mobile;
    @NotBlank(groups = SMSLoginGroup.class)
    private String smsCode;
}
