package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.SysRoleMapper;
import com.cxylm.springboot.model.system.Role;
import com.cxylm.springboot.model.system.RoleMenu;
import com.cxylm.springboot.model.system.UserRole;
import com.cxylm.springboot.service.SysRoleMenuService;
import com.cxylm.springboot.service.SysRoleService;
import com.cxylm.springboot.service.SysUserRoleService;
import com.cxylm.springboot.systemdto.form.RoleForm;
import com.cxylm.springboot.util.RedisCacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, Role> implements SysRoleService {
    private final SysRoleMenuService sysRoleMenuService;
    private final SysUserRoleService sysUserRoleService;
    private final RedisCacheUtil redisCacheService;

    @Override
    public void addRole(RoleForm roleForm) {
        final Role role = new Role();
        BeanUtils.copyProperties(roleForm, role);
        this.save(role);

        saveRoleMenus(roleForm.getMenuIds(), role.getId());
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeById(roleId);
        sysRoleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
        sysUserRoleService.remove(new QueryWrapper<UserRole>().eq("role_id", roleId));
    }

    @Override
    public boolean updateRole(RoleForm roleForm) {
        final Role role = new Role();
        BeanUtils.copyProperties(roleForm, role);
        final boolean updated = this.updateById(role);
        if (!updated) {
            return false;
        }

        sysRoleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", roleForm.getId()));
        saveRoleMenus(roleForm.getMenuIds(), role.getId());
        return true;
    }

    @Override
    public IPage<Role> getRoleList(Page<Role> page) {
        return baseMapper.getRoleList(page);
    }

    @Override
    public List<Role> findUserRole(Integer userId) {
        final List<Role> userRoles = baseMapper.findUserRole(userId);
//        redisCacheService.set(CacheName.SYS_USER_ROLE, String.valueOf(userId), userRoles);
        return userRoles;
    }

    private void saveRoleMenus(final List<Long> menuIds, final Long roleId) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return;
        }
        final List<RoleMenu> collect = menuIds.stream().map(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            return roleMenu;
        }).collect(Collectors.toList());
        sysRoleMenuService.saveBatch(collect);
    }
}
