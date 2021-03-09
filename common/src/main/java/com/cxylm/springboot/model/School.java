package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * 学区信息
 * @author HaoTi
 */
@Getter
@Setter
@TableName("school")
public class School extends Model<School> {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学区名称
     */
    private String name;

    /**
     * 地址
     */
    private String addr;

    private Boolean enabled;
}
