package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 单词
 */
@Getter
@Setter
public class WordsSelectionForm {

    /**
     * 搜索课程名
     */
    @Length(max = 255, message = "搜索课程名最多255字")
    private String name;
}
