package com.cxylm.springboot.dto.category;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Author: shiyanru
 * @Date: 2019/9/26 22:46
 */
@Getter
@Setter
public class OpenCourseDto {
    @NotNull(message = "请输入卡号")
    private String no;
    @NotNull(message = "请输入密码")
    private String password;
    @NotNull(message = "请选择开通课程")
    private Integer bookId;
}
