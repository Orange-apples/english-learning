package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.annotation.validator.MobilePhone;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 词库添加
 */
@Getter
@Setter
public class SchoolRegisterForm {

    private Integer id;

    @NotBlank(message = "请输入密码")
    private String username;
    @NotBlank(message = "请输入密码")
    private String pwd;

    /**
     * 校区名称
     */
    @NotBlank(message = "请输入校名称")
    @Length(max = 32, message = "校名称不能超过32个字")
    private String schoolName;

    @MobilePhone
    private String mobile;

    /**
     * 地址
     */
    @NotBlank(message = "请输入地址")
    @Length(max = 255, message = "地址名称不能超过255个字")
    private String schoolAddr;
}
