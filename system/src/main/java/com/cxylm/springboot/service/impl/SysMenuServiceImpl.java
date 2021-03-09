package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.SysMenuMapper;
import com.cxylm.springboot.model.system.Menu;
import com.cxylm.springboot.model.system.RoleMenu;
import com.cxylm.springboot.service.SysMenuService;
import com.cxylm.springboot.service.SysRoleMenuService;
import com.cxylm.springboot.systemdto.form.MenuForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cxylm.springboot.constant.Constant.SUPER_ADMIN_ROLE_ID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, Menu> implements SysMenuService {
    private final SysRoleMenuService sysRoleMenuService;

    @Override
    public List<Menu> getAllMenus() {
        return this.list();
    }

    @Override
    public List<Menu> findUserPermissions(Integer userId) {
        return baseMapper.findUserPermissions(userId);
    }

    @Override
    public List<Menu> findUserMenus(Integer userId, boolean menuOnly) {
        return this.baseMapper.findUserMenus(userId, menuOnly);
    }

    @Override
    public List<Menu> findRoleMenus(Long roleId) {
        return this.baseMapper.findRoleMenus(roleId);
    }

    @Override
    public void addNewMenu(MenuForm form) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(form, menu);
        menu.setId(null);
        this.save(menu);

        // 超级管理员角色自动拥有该权限
        RoleMenu rm = new RoleMenu();
        rm.setMenuId(menu.getId());
        rm.setRoleId(SUPER_ADMIN_ROLE_ID);
        sysRoleMenuService.save(rm);
    }

    @Override
    public void deleteMenu(Long menuId) {
        sysRoleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id", menuId));
        this.removeById(menuId);
        // TODO remove redis cache
    }
}
