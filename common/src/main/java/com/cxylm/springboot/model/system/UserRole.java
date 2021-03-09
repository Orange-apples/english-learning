package com.cxylm.springboot.model.system;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 * <p>
 * 系统用户角色关联表
 * </p>
 *
 * @author Elva
 */
@TableName("sys_user_role")
@Getter
@Setter
@ToString
public class UserRole extends Model<UserRole> {
    private static final long serialVersionUID = -7760999004657301187L;
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 序号
     */
    private Integer userId;
    /**
     * 父角色id
     */
    private Long roleId;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
