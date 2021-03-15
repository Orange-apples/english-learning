package com.cxylm.springboot.dto;

import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 后端用户列表管理
 *
 * @author HaoTi
 */
@Getter
@Setter
public class UserManageDto {
    private Integer id;

    private boolean merchant;

    /**
     * 手机号
     */
    private String mobile;
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 身份
     */
    private Integer rank;

    /**
     * 性别
     */
    private Gender sex;

    /**
     * 账号状态
     */
    private AccountState accountState;
    /**
     * 微信openId
     */
    private String wxOpenId;

    private Date createTime;
    private Integer schoolId;
}
