package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 听读训练校对
 */
@Getter
@Setter
public class ListenCheckForm {

    /**
     * 单词
     */
    @NotNull
    private String word;

    /**
     * 语音文件
     */
    @NotNull
    private MultipartFile file;
}
