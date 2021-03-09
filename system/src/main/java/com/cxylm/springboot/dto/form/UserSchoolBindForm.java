package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserSchoolBindForm {
    @NotNull(message = "请选择一个学校")
    private Integer schoolId;
    @NotNull(message = "请选择一个用户")
    private Integer userId;
}
