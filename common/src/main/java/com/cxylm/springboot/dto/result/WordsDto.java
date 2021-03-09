package com.cxylm.springboot.dto.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordsDto {
    /**
     * 单词id
     */
    private Integer wordId;
    /**
     * 英语单词
     */
    private String word;

    /**
     * 汉语
     */
    private String mean;

    /**
     * 音标，美
     */
    private String symbolA;

    /**
     * 音标，英
     */
    private String symbolB;
}
