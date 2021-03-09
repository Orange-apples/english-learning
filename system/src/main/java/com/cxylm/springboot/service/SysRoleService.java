package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.model.system.Role;
import com.cxylm.springboot.systemdto.form.RoleForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统角色Service
 */
public interface SysRoleService extends IService<Role> {
    @Transactional
    void addRole(RoleForm roleForm);

    @Transactional
    void deleteRole(Long roleId);

    @Transactional
    boolean updateRole(RoleForm roleForm);

    IPage<Role> getRoleList(Page<Role> page);

    List<Role> findUserRole(Integer userId);
}
