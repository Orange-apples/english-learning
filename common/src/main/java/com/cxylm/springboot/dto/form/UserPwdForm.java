package com.cxylm.springboot.dto.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wbh
 * @date 2020/11/7 11:33
 * @description 修改用户账号密码
 */
@Data
public class UserPwdForm {

    /**原密码*/
    @NotBlank
    private String oldPwd;

    /**新密码*/
    @NotBlank
    private String newPwd;
}
