package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName
public class CourseCardBind extends Model<CourseCardBind> {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long cardId;
    private Integer bookId;
    private Integer bindUserId;
}
