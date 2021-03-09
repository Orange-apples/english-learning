package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.EnableState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Getter
@Setter
@TableName("sys_bd")
public class BD extends Model<BD> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 昵称
     */
    private String name;

    /**
     * 账号状态
     */
    private EnableState del;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date createTime;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date updateTime;
}
