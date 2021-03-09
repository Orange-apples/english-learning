package com.cxylm.springboot.dto.result;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author: shiyanru
 * @Date: 2020/3/9 17:34
 */
@Getter
@Setter
public class LoginRecordDto {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 登录时间
     */
    private String loginTime;

    /**
     * 访问次数
     */
    private Integer count;
}
