package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.enums.TestType;
import com.cxylm.springboot.enums.WordType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 综合测试
 */
@Getter
@Setter
public class WordsTestForm {

    /**
     * 0生词，2熟词
     */
//    private WordType wordType;

    /**
     * 5单元测试，6一测到底，7学前测试，8学后测试
     */
    private TestType testType;

    /**
     * 1汉译英，2英译汉，3听选，4听写，
     */
    private List<TestType> autoTestType;


    /**
     * 每组试题数
     */
    private Integer size;

    /**
     * 课本id
     */
    private Integer bookId;

    /**
     * unitId
     */
    private Integer unitId;
}
