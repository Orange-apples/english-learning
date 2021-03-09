package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.enums.StudyType;
import lombok.Getter;
import lombok.Setter;

/**
 * 学习
 */
@Getter
@Setter
public class StudyForm {

    /**
     * 课程id
     */
    private Integer bookId;

    /**
     * unitId
     */
    private Integer unitId;

    /**
     * 0、复习，1、单词学习，2、智能听写，3、听读训练（默认为0复习）
     */
    private StudyType studyType;
}
