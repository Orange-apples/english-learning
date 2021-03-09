package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OpenCourseBatchForm {
    @NotNull(message = "请选择课程阶段")
    private Integer level;
    @NotNull(message = "请选择出版社")
    private String publisher;
    @NotNull(message = "请选择用户")
    private Integer userId;
    private Long expireTime;
}
