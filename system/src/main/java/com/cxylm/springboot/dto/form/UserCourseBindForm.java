package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserCourseBindForm {
    @NotNull(message = "请选择一个课程")
    private Integer courseId;
    @NotNull(message = "请选择一个用户")
    private Integer userId;
    private Long expireTime;
}
