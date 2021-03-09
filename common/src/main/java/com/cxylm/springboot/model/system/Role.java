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
 * 系统角色表
 * </p>
 *
 * @author Elva
 */
@TableName("sys_role")
@Getter
@Setter
@ToString
public class Role extends Model<Role> {
    private static final long serialVersionUID = 5772588786093147130L;
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 序号
     */
    private Integer orderNum;
    /**
     * 父角色id
     */
    private Integer pid;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 提示
     */
    private String remark;

    @Version
    private Integer version;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
//    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date updateTime;

//    @TableField(fill = FieldFill.UPDATE)
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
