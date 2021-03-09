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
@TableName("course_open_record")
public class CourseOpenRecord extends Model<CourseOpenRecord> {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer bdId;
    private Integer courseId;
    private Integer userId;
    private Integer coursePrice;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date createTime;
}
