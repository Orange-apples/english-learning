package com.cxylm.springboot.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.dao.SysUserMapper;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.model.system.Menu;
import com.cxylm.springboot.model.system.Role;
import com.cxylm.springboot.model.system.User;
import com.cxylm.springboot.model.system.UserRole;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.SysMenuService;
import com.cxylm.springboot.service.SysRoleService;
import com.cxylm.springboot.service.SysUserRoleService;
import com.cxylm.springboot.service.SysUserService;
import com.cxylm.springboot.systemdto.RouterMeta;
import com.cxylm.springboot.systemdto.SysUserDto;
import com.cxylm.springboot.systemdto.VueRouter;
import com.cxylm.springboot.systemdto.form.SysUserForm;
import com.cxylm.springboot.util.RedisCacheUtil;
import com.cxylm.springboot.util.vue.TreeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cxylm.springboot.constant.AppMessage.ERROR_RECORD_NOT_EXIST;
import static com.cxylm.springboot.constant.Constant.DEFAULT_ICON;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, User> implements SysUserService {
    private final RedisCacheUtil redisCacheUtil;
    private final SysRoleService sysRoleService;
    private final SysMenuService sysMenuService;
    private final SysUserRoleService sysUserRoleService;

    @Override
    public IPage<SysUserDto> getUserList(Page<SysUserDto> page) {
        return baseMapper.getUserList(page);
    }

    @Override
    // @Cacheable(cacheNames = CacheName.SYS_USER, key = "#username")
    public User findByUserName(String username) {
        return this.getOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public Set<String> getUserRoles(Integer userId) {
        List<Role> roleList = redisCacheUtil.selectCacheByTemplate(
                () -> (List<Role>) this.redisCacheUtil.get(CacheName.SYS_USER_ROLE, String.valueOf(userId)),
                () -> this.sysRoleService.findUserRole(userId));
        return roleList.stream().map(Role::getName).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getUserPermissions(Integer userId) {
        List<Menu> permissionList = redisCacheUtil.selectCacheByTemplate(
                () -> (List<Menu>) this.redisCacheUtil.get(CacheName.SYS_USER_PERMISSION, String.valueOf(userId)),
                () -> this.sysMenuService.findUserPermissions(userId));
        return permissionList.stream().map(Menu::getPermission).collect(Collectors.toSet());
    }

    @Override
    public ArrayList<VueRouter<Menu>> getUserRouters(Integer userId) {
        List<VueRouter<Menu>> routes = new ArrayList<>();
        List<Menu> menus = this.sysMenuService.findUserMenus(userId, true);
        menus.forEach(menu -> {
            VueRouter<Menu> route = new VueRouter<>();
            route.setId(menu.getId());
            route.setPid(menu.getPid());
            route.setIcon(menu.getIcon());
            route.setPath(menu.getPath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getName());
            route.setMeta(new RouterMeta(true, null));
            routes.add(route);
        });
        return TreeUtil.buildVueRouter(routes);
    }

    @Override
    public void addNewUser(SysUserForm form) {
        User user = new User();
        BeanUtils.copyProperties(form, user);
        user.setState(AccountState.NORMAL);
        user.setAvatar(DEFAULT_ICON);
        user.setSalt(RandomStringUtils.randomAlphanumeric(6));
        // handle password
        user.setPassword(DigestUtil.sha256Hex(form.getPassword() + user.getSalt()));

        this.save(user);

        saveUserRole(form, user);
    }

    private void saveUserRole(SysUserForm form, User user) {
        final List<Long> roleIds = form.getRoleIds();
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }

        final List<UserRole> userRoles = roleIds.stream().map(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            return userRole;
        }).collect(Collectors.toList());
        sysUserRoleService.saveBatch(userRoles);
    }

    @Override
    public ResponseEntity<?> updateUser(Integer userId, SysUserForm form) {
        final User user = this.getById(userId);
        if (user == null) {
            return AppResponse.badRequest(ERROR_RECORD_NOT_EXIST);
        }
        BeanUtils.copyProperties(form, user, "password");
        // handle password
        final String password = form.getPassword();
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(DigestUtil.sha256Hex(form.getPassword() + user.getSalt()));
        }

        final boolean b = this.updateById(user);
        if (!b) {
            return AppResponse.badRequest(ERROR_RECORD_NOT_EXIST);
        }
        sysUserRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));

        saveUserRole(form, user);
        // TODO clear cache
        return AppResponse.ok();
    }

    @Override
    public ResponseEntity<?> deleteUser(Integer userId) {
        final User user = this.getById(userId);
        if (user == null) {
            return AppResponse.badRequest(ERROR_RECORD_NOT_EXIST);
        }

//        user.setState(AccountState.LOGOUT);
//        final boolean b = this.updateById(user);
        boolean b = this.removeById(userId);
        if (!b) {
            return AppResponse.badRequest(ERROR_RECORD_NOT_EXIST);
        }
        sysUserRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));
        // TODO clear cache
        return AppResponse.ok();
    }
}
