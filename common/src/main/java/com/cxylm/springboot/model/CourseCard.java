package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Getter
@Setter
@TableName
public class CourseCard extends Model<CourseCard> {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 卡号
     */
    private Long no;
    private String password;
    private Integer courseId;
    private String name;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date createTime;
    private Boolean used;
    private Integer useCount;
    private Long expireTime;
    private Integer courseLevel;
    private String coursePublisher;

    /**
     * 业务员id
     */
    private Integer sysBdId;
}
