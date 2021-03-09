package com.cxylm.springboot.dto.form;

import com.cxylm.springboot.enums.AccountState;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 用户
 * 封禁/解封
 * @author HaoTi
 */
@Getter
@Setter
public class UserStateForm {

    /**
     * 用户id
     */
    @NotNull(message = "用户id为空")
    private Integer userId;

    /**
     * 用户状态
     */
    @NotNull(message = "为选择用户状态")
    private AccountState state;

}
