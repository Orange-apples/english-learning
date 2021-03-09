package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 选课
 */
@Getter
@Setter
public class CourseSelectionForm {

    /**
     * 搜索课程名
     */
    @Length(max = 255, message = "搜索课程名最多255字")
    private String name;

    /**
     * 版本
     */
    @Length(max = 255, message = "版本内容最多255字")
    private String edition;

    private String publisher;
    private Integer level;

    /**
     * 用户id,查询课程是否开通
     */
    private Integer userId;
}
