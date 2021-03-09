package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("system_config")
public class SystemConfig extends Model<SystemConfig> {
    private static final long serialVersionUID = 6469162255935923741L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("`key`")
    private String key;

    private String value;
}
