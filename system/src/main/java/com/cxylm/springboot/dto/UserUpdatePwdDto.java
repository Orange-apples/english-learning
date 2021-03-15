package com.cxylm.springboot.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @auther Orange-apples
 * @date 2021/3/13 22:19
 */
@Data
public class UserUpdatePwdDto {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "请输入验证码")
    private String code;
    @NotBlank(message = "请输入新密码")
    private String newPwd;
}
