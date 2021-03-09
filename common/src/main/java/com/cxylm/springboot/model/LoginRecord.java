package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户登录记录
 * @Author: shiyanru
 * @Date: 2020/3/9 17:08
 */
@Getter
@Setter
@TableName("login_record")
public class LoginRecord extends Model<LoginRecord> {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;
    private Integer schoolId;

    /**
     * 登录时间
     */
    private Date loginTime;
}
