package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
public class CourseCardForm {
    @NotEmpty(message = "请输入卡名")
    @Length(max = 63, message = "卡名最多63位")
    private String name;
    //    @NotNull(message = "请输入卡号")
//    private Long no;
//    @NotBlank(message = "请输入密码")
//    @Length(max = 16, message = "密码最多16位")
//    private String password;
    private Long expireTime;

    private boolean isBindAll;

    // 阶段
    private Integer courseLevel;

    // 版本
    private String coursePublisher;
    /**
     * 生成数量
     */
    @Positive
    @Max(value = 100, message = "受性能限制, 目前最多一次批量生成100个")
    private Integer amount = 1;

    /**
     * 绑定的业务员id
     */
    private Integer sysBdId;
}
