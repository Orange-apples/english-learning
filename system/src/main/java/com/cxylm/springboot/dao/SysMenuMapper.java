package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.model.system.Menu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<Menu> {
    IPage<Menu> getMenuList(Page<Menu> page);

    List<Menu> findUserPermissions(Integer userId);

    List<Menu> findUserMenus(Integer userId, boolean menuOnly);

    List<Menu> findRoleMenus(Long roleId);
}
