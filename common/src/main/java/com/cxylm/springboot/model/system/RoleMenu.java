package com.cxylm.springboot.model.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 系统角色权限关联表
 * </p>
 *
 * @author Elva
 */
@TableName("sys_role_menu")
@Getter
@Setter
@ToString
public class RoleMenu extends Model<RoleMenu> {
    private static final long serialVersionUID = -7943073016632584870L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long menuId;
    private Long roleId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
