package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.model.system.Menu;
import com.cxylm.springboot.model.system.User;
import com.cxylm.springboot.systemdto.SysUserDto;
import com.cxylm.springboot.systemdto.VueRouter;
import com.cxylm.springboot.systemdto.form.SysUserForm;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;

/**
 * 系统用户Service
 */
public interface SysUserService extends IService<User> {
    IPage<SysUserDto> getUserList(Page<SysUserDto> page);

    User findByUserName(String username);

    /**
     * 通过用户名获取用户角色集合
     *
     * @param userId 用户ID
     * @return 角色集合
     */
    Set<String> getUserRoles(Integer userId);

    /**
     * 通过用户名获取用户权限集合
     *
     * @param userId 用户
     * @return 权限集合
     */
    Set<String> getUserPermissions(Integer userId);

    ArrayList<VueRouter<Menu>> getUserRouters(Integer userId);

    /**
     * 新增账号
     *
     * @param form 表单
     */
    @Transactional
    void addNewUser(SysUserForm form);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    ResponseEntity<?> updateUser(Integer userId, SysUserForm form);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    ResponseEntity<?> deleteUser(Integer userId);
}
