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
@TableName("sys_menu")
@Getter
@Setter
@ToString
public class Menu extends Model<Menu> {
    private static final long serialVersionUID = -4072140933034114811L;
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 父角色id
     */
    private Long pid;
    private Integer type;
    private Integer status;
    /**
     * 序号
     */
    private Integer orderNum;
    /**
     * 角色名称
     */
    private String name;

    private String icon;
    private String path;
    private String component;
    private String permission;
    private String remark;

    @Version
    private Integer version;

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
