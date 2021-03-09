package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cxylm.springboot.enums.MemoryLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 已学单词记录
 * @author HaoTi
 */
@Getter
@Setter
@TableName("study_word_records")
public class StudyWordRecords extends Model<StudyWordRecords> {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 单词id
     */
    private Integer wordId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 单词级别，0-5,0为生词，1-4为一般词，5为熟词
     * 0、一天以内，1、一天，2、两天，3、四天，4、七天，5、十五天
     */
    private MemoryLevel level;

    /**
     * 错误次数
     */
    private Integer errorTimes;

    /**
     * 记忆时间，（单词级别判断使用）
     */
    private Date memoryTime;

    /**
     * 上次错误时间，（智能拼写使用，获取近期错误单词）
     */
    private Date lastErrorTime;

    /**
     * 测试次数
     */
    private Integer testTimes;
}
