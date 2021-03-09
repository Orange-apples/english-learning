package com.cxylm.springboot.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.dto.form.LoginForm;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.system.Role;
import com.cxylm.springboot.model.system.User;
import com.cxylm.springboot.properties.SystemConfigProperties;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.SysRoleService;
import com.cxylm.springboot.service.SysUserService;
import com.cxylm.springboot.systemdto.SysUserDto;
import com.cxylm.springboot.systemdto.form.SysUserForm;
import com.cxylm.springboot.util.JwtHelper;
import com.cxylm.springboot.util.RedisCacheUtil;
import com.cxylm.springboot.validator.group.AddGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserController extends ApiController {
    private final JwtHelper jwtTokenUtil;
    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final RedisCacheUtil redisService;
    private final SystemConfigProperties sysProps;

    @PostMapping("/user/login")
    public Object login(@RequestBody @Validated LoginForm form) {
        final User user = sysUserService.findByUserName(form.getUsername());
        if (user == null || !user.getPassword().equals(DigestUtil.sha256Hex(form.getPassword() + user.getSalt()))) {
            return AppResponse.badRequest("用户名或密码错误");
        }

        if (user.getState() != AccountState.NORMAL) {
            return AppResponse.badRequest("账号已被冻结");
        }

        int userId = user.getId();
        final String token = jwtTokenUtil.generateManageToken(userId, user.getUsername());
        redisService.set(CacheName.SHIRO_TOKEN_PREFIX, String.valueOf(userId), getIP(), 6 * 60 * 60);
        //更新用户的登录时间
        user.setLastLogin(new Date());
        sysUserService.updateById(user);
        return AppResponse.ok(new JwtResponse(token, userId));
    }

    @PostMapping("/user/logout")
    public Object logout() {
        return SUCCESS;
    }

    @GetMapping({"/user/info"})
    public Object getUserInfo() {
        final Integer userId = getUserId();
        Map<String, Object> res = new HashMap<>();
        final User user = sysUserService.getById(userId);
        final List<Long> roleIds =
                sysRoleService.findUserRole(userId).stream().map(Role::getId).collect(Collectors.toList());
        res.put("roles", roleIds);
        res.put("introduction", "");
        final String avatar = user.getAvatar();
        res.put("avatar", sysProps.getZimgAccess() + (avatar == null ? "9ce4f9b6abd139adb3f1fd813951563c" : avatar));
        res.put("name", user.getName());
        res.put("routers", sysUserService.getUserRouters(userId));
        res.put("permissions", sysUserService.getUserPermissions(userId));
        return AppResponse.ok(res);
    }

    @RequiresPermissions("user:add")
    @PostMapping("/user")
    public Object addUser(@RequestBody @Validated({AddGroup.class, Default.class}) SysUserForm form) {
        final User user = sysUserService.findByUserName(form.getUsername());
        if (user != null) {
            return AppResponse.badRequest("用户名已存在");
        }
        sysUserService.addNewUser(form);
        return SUCCESS;
    }

    @RequiresPermissions("user:view")
    @GetMapping("/users")
    public Object getUsers() {
        final IPage<SysUserDto> users = sysUserService.getUserList(ApiPageFactory.getPage());
        return AppResponse.ok(users);
    }

    @RequiresPermissions("user:view")
    @GetMapping("/user/{userId}")
    public Object getUserDetail(@PathVariable Integer userId) {
        return BAD_REQUEST;
    }

    @RequiresPermissions("user:update")
    @PutMapping("/user/{userId}")
    public Object updateUser(@PathVariable Integer userId, @RequestBody @Validated SysUserForm form) {
        return sysUserService.updateUser(userId, form);
    }

    @RequiresPermissions("user:delete")
    @DeleteMapping("/user/{userId}")
    public Object deleteUser(@PathVariable Integer userId) {
        return sysUserService.deleteUser(userId);
    }

    @Data
    @AllArgsConstructor
    private static class JwtResponse {
        private String token;
        private Integer userId;
    }
}
