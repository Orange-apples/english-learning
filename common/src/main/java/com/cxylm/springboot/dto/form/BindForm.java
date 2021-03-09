package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BindForm {
    @NotBlank(message = "openID不能为空")
    private String openId;
}
