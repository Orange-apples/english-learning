package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cxylm.springboot.enums.StudyRateState;
import lombok.Getter;
import lombok.Setter;

/**
 * 学习课程表
 * @author HaoTi
 */
@Getter
@Setter
@TableName("study_book_rate")
public class StudyBookRate extends Model<StudyBookRate> {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 课本id
     */
    private Integer bookId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * unitId
     */
    private Integer unitId;

    /**
     * 是否学习，0、未学，1、学习中，2、单词学习完成，3、听读训练完成，4、单元训练完成，5、听读训练和单元训练完成
     */
    private StudyRateState state;

    /**
     * 开通时间
     */
    private Long createTime;

    /**
     * 上次学习到的id
     */
    private Integer lastWordsId;

    /**
     * 学习过期时间, 到达此时间后只能复习, 不能学习
     */
    private Long expireTime;

    /**
     * 复习过期时间, 当到达此时间后, 课程才是真正过期了, 会被删除所有记录
     */
    private Long reviewExpireTime;

    /**
     * 是否为试用
     */
    private Integer isTry;
}
