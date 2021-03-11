package com.cxylm.springboot.dto.result;

import com.cxylm.springboot.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 账户设置dto
 */
@Data
public class UserInfoDto {

    private Integer id;
    
    /**昵称*/
    private String nickname;
    private String avatar;


    /**性别*/
    private Gender gender;

    /**金币*/
    private Integer coin;
    /**生日*/
    private Date birthday;
    private String wxOpenId;

    /**注册时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date createTime;
}
