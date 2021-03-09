package com.cxylm.springboot.systemdto.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoleForm {

    /**
     * 角色id
     */
    private Long id;
    /**
     * 角色排序序号
     */
    @NotNull(message = "序号不能为空")
    private Integer orderNum;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String name;
    /**
     * 角色备注
     */
    private String remark;

    private Integer version;

    /**
     * 角色拥有权限列表
     */
    private List<Long> menuIds;
}
