package com.cxylm.springboot.dto.result;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cxylm.springboot.enums.TestType;
import lombok.Getter;
import lombok.Setter;

/**
 * 测试记录
 *
 * @author HaoTi
 */
@Getter
@Setter
public class TestResultDto extends Model<TestResultDto> {

    /**
     * 课程名
     */
    private String bookName;
    /**
     * 0否，1是，
     */
    private Integer score;

    /**
     * 测试时间（秒）
     */
    private Boolean pass;

    /**
     * 1汉译英，2英译汉，3听选，4听写，5单元测试，6一测到底，7学前测试，8学后测试
     */
    private TestType testType;

    /**
     * 书本的id
     */
    private Integer bookId;

    /**
     * 单元id
     */
    private Integer unitId;

    /**
     * 测试时间（秒）
     */
    private Integer times;

    /**
     * 测试日期
     */
    private String createTime;

    /**
     * 对应的章节单词数量
     */
    private Integer wordCount;
    /**
     * 是否为普通测试
     */
    private Integer normalTest;
}
