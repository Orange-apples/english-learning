package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static cn.hutool.core.date.DatePattern.NORM_DATE_PATTERN;

/**
 * 账户设置from
 */
@Data
public class UserInfoForm {

    /**昵称*/
    @NotNull(message = "昵称不能为空")
    @Size(max = 16,message = "昵称长度请在16个字符以内")
    private String nickname;

    private String avatar;

    /**性别*/
    @NotNull
    private Gender gender;

    /**生日*/
    @NotNull
    @JsonFormat(pattern = NORM_DATE_PATTERN)
    private Date birthday;

    /**原密码*/
    private String oldPwd;

    /**新密码*/
    private String newPwd;
}
