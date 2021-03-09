package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.enums.StudyType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 综合学习完成
 */
@Getter
@Setter
public class StudySaveForm {

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
    /**
     * 单词列表
     */
    @NotNull
    private List<StudySaveWordsForm> words;
    /**
     * 识别时间（秒）
     */
    @NotNull
    private Integer learningTime;
    /**
     * 拼写时间（秒）
     */
    @NotNull
    private Integer spellTime;
}
