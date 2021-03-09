package com.cxylm.springboot.systemdto;

import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
public class SysUserDto implements Serializable {
    private static final long serialVersionUID = -4049195483824336493L;

    private Integer id;

    private String avatar;

    private String username;

    @JsonIgnore
    private String password;

    private String salt;

    private String name;

    private Date birthday;

    private Gender gender;

    private String email;

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

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date createTime;

    private String createUser;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date updateTime;

    private String updateUser;

    private Integer version;

    private List<Long> roleIds;

    private List<String> roleNames;
}
