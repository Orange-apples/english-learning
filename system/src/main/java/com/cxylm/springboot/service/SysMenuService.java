package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.model.system.Menu;
import com.cxylm.springboot.systemdto.form.MenuForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统菜单Service
 */
public interface SysMenuService extends IService<Menu> {
    List<Menu> getAllMenus();

    List<Menu> findUserPermissions(Integer userId);

    /**
     * 检索用户所有权限
     * @param userId 用户id
     * @param menuOnly 是否仅检索菜单类型（不含按钮型权限）
     * @return 用户所有权限
     */
    List<Menu> findUserMenus(Integer userId, boolean menuOnly);

    List<Menu> findRoleMenus(Long roleId);

    @Transactional
    void addNewMenu(MenuForm form);

    @Transactional
    void deleteMenu(Long menuId);
}
