package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 词库添加
 */
@Getter
@Setter
public class AddWordsForm {

    /**
     * 课本名称
     */
    @NotNull
    private String name;

    /**
     * 阶段（1、小学英语，2、初中英语，3、高中英语，4、大学英语，5、托福/雅思）
     */
    @NotNull
    private Integer level;

    /**
     * 版本
     */
    @NotNull
    @Length(max = 255, message = "版本内容最多255字")
    private String edition;

    /**
     * 出版社
     */
    @NotNull
    @Length(max = 255, message = "出版社内容最多255字")
    private String press;

    private String detail = "";

    /**
     * 单词数据，Excel文件
     */
    @NotNull
    private MultipartFile file;

    private Boolean free;
}
