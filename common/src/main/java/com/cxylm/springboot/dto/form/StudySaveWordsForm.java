package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 综合学习完成--单词
 */
@Getter
@Setter
public class StudySaveWordsForm {

    /**
     * 单词id
     */
    @NotNull
    private Integer wordId;

    /**
     * 错误次数
     */
    @NotNull
    private Integer errorTimes;

    /**
     * 0、一天以内，1、一天，2、两天，3、四天，4、七天，5、十五天
     */
//    @Null
    private Integer level;
}
