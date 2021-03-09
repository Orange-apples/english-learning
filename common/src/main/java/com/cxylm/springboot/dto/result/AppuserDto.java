package com.cxylm.springboot.dto.result;

import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author: shiyanru
 * @Date: 2020/3/9 16:28
 */

@Getter
@Setter
public class AppuserDto {

    private Integer id;

    /** 用户名 */
    private String username;

    /**手机号*/
    private String phone;

    /**昵称*/
    private String nickname;

    /**头像*/
    private String avatar;

    /**性别*/
    private Gender gender;

    /**账号状态*/
    private AccountState accountState;

    /**
     * 用户注册日期
     */
    private Date createTime;

    /**
     * 查询数量
     */
    private Integer count;

    /**
     * 最后一次学习的课程名称
     */
    private String bookName;

    /**
     * 最后一次学习的课程单词数量
     */
    private Integer wordsCount;

    /**
     * 课程评分
     */
    private Integer score;
}
