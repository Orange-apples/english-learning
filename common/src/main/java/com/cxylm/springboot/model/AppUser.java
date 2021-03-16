package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.AccountType;
import com.cxylm.springboot.enums.Gender;
import com.cxylm.springboot.enums.StudyState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Getter
@Setter
@TableName("app_user")
@RedisHash(value = "AppUser", timeToLive = 60 * 30)
public class AppUser extends Model<AppUser> {
    private static final long serialVersionUID = -2129148219351361369L;
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 账户角色（0 学生/家长  1 校长）
     */
    private AccountType merchant;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Gender gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 地址
     */
    private String address;

//    private String salt;

    /**
     * 账号状态
     */
    private AccountState accountState;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    private Date createTime;

    /**
     * 支付密码
     */
    private String pwdPay;

    /**
     * 登录密码
     */
    private String pwd;

    private Long logoutTime;

    /**
     * 学生学习状态
     */
    /**
     * 上次学到的单词id
     */
    private Integer lastWordId;
    /**
     * 正在学习的课程id
     */
    private Integer bookId;
    /**
     * 正在学习的单元id
     */
    private Integer unitId;
    /**
     * 状态，0、未开始，1、学习中，2、已完成
     */
    private StudyState unitState;
    /**
     * 听读训练UnitId
     */
    private Integer listenUnitId;
    /**
     * 识别时间（秒）
     */
    private Integer learningTime;
    /**
     * 拼写时间（秒）
     */
    private Integer spellTime;
    /**
     * 金币
     */
    private Integer coin;
    /**
     * 测试时间（秒）
     */
    private Integer testTime;

    private Integer schoolId;

    @TableLogic(delval = "1")
    private Integer isDel;

    /**
     * 推广人（业务员）代码
     */
    private Integer bdCode;
    private String schoolName;
    private String schoolAddr;
    private Integer schoolState;
    private String wxOpenId;
    private Long expire;

    /**
     * 上次复习时间
     */
    private Long lastReviewTime;
    /**
     * 上次登录时间
     */
    private Date lastLoginTime;
}
