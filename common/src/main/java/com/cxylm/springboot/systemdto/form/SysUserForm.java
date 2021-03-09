package com.cxylm.springboot.systemdto.form;

import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.Gender;
import com.cxylm.springboot.validator.group.AddGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class SysUserForm {
    /**
     * 头像
     */
    private String avatar;
    /**
     * 账号
     */
    @NotBlank(message = "请输入用户名")
    private String username;
    /**
     * 密码
     */
    @NotBlank(groups = AddGroup.class, message = "请输入密码")
    private String password;

    /**
     * 名字
     */
    @NotBlank(message = "请输入姓名")
    private String name;
    /**
     * 生日
     */
    private Date birthday;

    private Gender gender = Gender.SECRET;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 电话
     */
    @NotBlank(message = "请输入电话")
    private String phone;

    /**
     * 部门id(多个逗号隔开)
     */
    private Integer deptId;
    /**
     * 状态(0：启用  1：冻结  2：删除）
     */
    private AccountState state;

    /**
     * 角色列表
     */
    private List<Long> roleIds;
}
