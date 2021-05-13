package com.cxylm.springboot.dto.bookrate;

import lombok.Data;

/**
 * @author wbh
 * @date 2020/11/18 10:00
 * @description
 */
@Data
public class BookStudyReport {

    private Integer bookId;

    /**
     * 课本名称
     */
    private String bookName;

    /**
     * 单元
     */
    private Integer unitId;

    /**
     * 单元下单词数量
     */
    private Integer wordsCount;

    /**
     * 是否通过
     */
    private boolean pass;
    /**
     *
     */
    private Integer lastWordsId;
}
