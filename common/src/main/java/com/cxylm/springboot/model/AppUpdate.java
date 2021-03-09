package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * app更新信息
 */
@Getter
@Setter
@TableName("app_update_record")
public class AppUpdate extends Model<AppUpdate> {
    private static final long serialVersionUID = 7401787561188843427L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String url;
    private Long createTime;
    private String log;
    private Boolean forceUpdate;
    private Integer minVersionCode;
    private String version;
    private Integer versionCode;
//    private Platform platform;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
