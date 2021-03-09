package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.enums.TestType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 综合测试保存
 */
@Getter
@Setter
public class WordsTestSaveForm {

    /**
     * 单词id列表
     */
    private List words;

    /**
     * 5单元测试，6一测到底，7学前测试，8学后测试
     */
    private TestType testType;

    /**
     * 1汉译英，2英译汉，3听选，4听写，
     */
    private List<TestType> autoTestType;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 测试时间（秒）
     */
    private Integer times;

    /**
     * 课本id
     */
    private Integer bookId;

    /**
     * unitId
     */
    private Integer unitId;
}
