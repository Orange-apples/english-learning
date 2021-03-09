package com.cxylm.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxylm.springboot.constant.AppMessage;
import com.cxylm.springboot.model.system.Menu;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.SysMenuService;
import com.cxylm.springboot.systemdto.form.MenuForm;
import com.cxylm.springboot.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysMenuController extends ApiController {
    private final SysMenuService sysMenuService;

    @RequiresPermissions("menu:add")
    @PostMapping("/menu")
    public Object addUser(@RequestBody @Validated MenuForm form) {
        if (form.getPid() != null) {
            final Menu pMenu = sysMenuService.getById(form.getPid());
            AssertUtil.badRequestWhenNull(pMenu, "选择的上级菜单不存在");
        }
        sysMenuService.addNewMenu(form);

        return SUCCESS;
    }

    @RequiresPermissions("menu:view")
    @GetMapping("/menus")
    public Object getUsers(@RequestParam(required = false) Integer userId) {
        return AppResponse.ok(userId == null ? sysMenuService.getAllMenus() : sysMenuService.findUserMenus(userId, false));
    }

    @RequiresPermissions(value = {"menu:view", "user:view"}, logical = Logical.OR)
    @GetMapping("/role/{roleId}/menus")
    public Object getRoleMenus(@PathVariable Long roleId) {
        return AppResponse.ok(sysMenuService.findRoleMenus(roleId));
    }

    @RequiresPermissions(value = {"menu:view", "user:view"}, logical = Logical.OR)
    @GetMapping("/user/{userId}/menus")
    public Object getUserMenus(@PathVariable Integer userId) {
        return AppResponse.ok(sysMenuService.findUserMenus(userId, true));
    }

    @RequiresPermissions("menu:view")
    @GetMapping("/menu/{menuId}")
    public Object getUsers(@PathVariable Long menuId) {
        return BAD_REQUEST;
    }

    @RequiresPermissions("menu:update")
    @PutMapping("/menu/{menuId}")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Object updateUser(@PathVariable Long menuId, @RequestBody @Validated MenuForm form) {
        final Menu menu = sysMenuService.getById(menuId);
        AssertUtil.badRequestWhenNull(menu, AppMessage.ERROR_RECORD_NOT_EXIST);
        BeanUtils.copyProperties(form, menu);
        final boolean updated = sysMenuService.updateById(menu);
        return updated ? SUCCESS : AppResponse.badRequest(AppMessage.ERROR_RECORD_OUTDATED);
    }

    @RequiresPermissions("menu:delete")
    @DeleteMapping("/menu/{menuId}")
    public Object deleteUser(@PathVariable Long menuId) {
        final int count = sysMenuService.count(new QueryWrapper<Menu>().eq("pid", menuId));
        if (count > 0) {
            return AppResponse.badRequest("该菜单存在下级菜单，请先删除其所有下级菜单");
        }
        sysMenuService.deleteMenu(menuId);
        return SUCCESS;
    }
}
