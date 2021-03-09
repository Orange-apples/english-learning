package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * 销售员
 * @author HaoTi
 */
@Getter
@Setter
@TableName("salesperson")
public class Salesperson extends Model<Salesperson> {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 销售员名称
     */
    private String name;
}
