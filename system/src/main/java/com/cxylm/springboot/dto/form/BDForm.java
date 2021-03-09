package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.EnableState;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BDForm {
    private Integer id;
    /**
     * 代码
     */
    private String code;

    /**
     * 手机号
     */
    @NotBlank(message = "输入营销员手机号")
    private String mobile;

    /**
     * 昵称
     */
    @NotBlank(message = "输入营销员姓名")
    private String name;

    /**
     * 账号状态
     */
    private EnableState del;
}
