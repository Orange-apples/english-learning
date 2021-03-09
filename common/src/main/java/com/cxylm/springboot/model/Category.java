package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @Author: shiyanru
 * @Date: 2019/9/10 22:13
 * 店铺类型model
 */
@TableName("category")
@Data
public class Category extends Model<Category> {
    /**
     * 主键序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父id
     */
    @TableField("p_id")
    private Integer pId;

    /**
     * 图标
     */
    @TableField("icon_url")
    private String iconUrl;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;

    /**
     * 类型级别
     */
    private Integer level;

    @TableField(exist = false)
    private Integer count;
}
