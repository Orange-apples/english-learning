package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cxylm.springboot.enums.TestType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 测试记录
 * @author HaoTi
 */
@Getter
@Setter
@TableName("study_test_records")
public class StudyTestRecords extends Model<StudyTestRecords> {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 测试时间（秒）
     */
    private Integer times;

    /**
     * 1汉译英，2英译汉，3听选，4听写，5单元测试，6一测到底，7学前测试，8学后测试
     */
    private TestType testType;

    /**
     * 课本id
     */
    private Integer bookId;

    /**
     * 单元id
     */
    private Integer unitId;

    /**
     * 测试日期
     */
    private Date createTime;
}
