package com.cxylm.springboot.dto.result;

import com.cxylm.springboot.enums.StudyRateState;
import lombok.Getter;
import lombok.Setter;

/**
 * 该课程各单元学习状况
 */
@Getter
@Setter
public class BooksUnitStateDto {
    /**
     * unitId
     */
    private Integer unitId;
    /**
     * 是否学习，0、未学，1、学习中，2、单词学习完成，3、听读训练完成，4、单元训练完成，5、听读训练和单元训练完成
     */
    private StudyRateState state;

    private Integer beforeScore;

    private Integer afterScore;
}
