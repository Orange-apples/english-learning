package com.cxylm.springboot.dto.result;

import com.cxylm.springboot.enums.StudyState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyStateDto {
    /**
     * 上次学到的单词
     */
    private String lastWord;
    /**
     * 正在学习的课程id
     */
    private Integer bookId;
    /**
     * 正在学习的课程名
     */
    private String bookName;
    /**
     * 正在学习的单元id
     */
    private Integer unitId;
    /**
     * 状态，0、未开始，1、学习中，2、已完成
     */
    private StudyState unitState;
    /**
     * 听读训练UnitId
     */
    private Integer listenUnitId;
}
