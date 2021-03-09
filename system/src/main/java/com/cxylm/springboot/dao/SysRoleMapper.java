package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.model.system.Role;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<Role> {
    IPage<Role> getRoleList(Page<Role> page);

    List<Role> findUserRole(Integer userId);
}
