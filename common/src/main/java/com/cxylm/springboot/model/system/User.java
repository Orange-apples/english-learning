package com.cxylm.springboot.model.system;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 * <p>
 * 管理员用户表
 * </p>
 *
 * @author Elva
 * 2019年11月16日 15:17:13
 */
@TableName("sys_user")
@Getter
@Setter
@ToString
public class User extends Model<User> {
    private static final long serialVersionUID = 5141992059683935265L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * md5密码盐
     */
    private String salt;
    /**
     * 名字
     */
    private String name;
    /**
     * 生日
     */
    private Date birthday;

    private Gender gender;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 电话
     */
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
     * 最后一次登录时间
     */
    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date lastLogin;

//    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updateUser;

    @Version
    private Integer version;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
