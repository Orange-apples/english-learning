package com.cxylm.springboot.systemdto.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MenuForm {
    private Long id;
    /**
     * 父菜单id
     */
    private Long pid;

    @NotNull
    private Integer type;
    private Integer status;
    /**
     * 序号
     */
    @NotNull
    private Integer orderNum;
    /**
     * 名称
     */
    @NotBlank
    private String name;

    private String icon;
    private String path;
    private String component;
    private String permission;
    private String remark;
    private Integer version;
}
