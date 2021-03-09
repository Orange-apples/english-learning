package com.cxylm.springboot.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TestWordsDto {
    /**
     * 单词id
     */
    private Integer wordId;
    /**
     * 问题
     */
    private String question;
    /**
     * 选项
     */
    private Map choices;
    /**
     * 答案
     */
    private String answer;
    /**
     * 单词
     */
    private String word;
    /**
     * 课本Id
     */
    @JsonIgnore
    private Integer bookId;
}
