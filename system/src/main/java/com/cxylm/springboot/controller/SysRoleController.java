package com.cxylm.springboot.controller;

import com.cxylm.springboot.constant.AppMessage;
import com.cxylm.springboot.controller.ApiController;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.system.Menu;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.SysMenuService;
import com.cxylm.springboot.service.SysRoleService;
import com.cxylm.springboot.systemdto.form.RoleForm;
import com.cxylm.springboot.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysRoleController extends ApiController {
    private final SysRoleService sysRoleService;
    private final SysMenuService sysMenuService;
    private static final String ERR_BUILT_IN_ROLE = "系统内置角色，拒绝操作";

    @RequiresPermissions("role:add")
    @PostMapping("/role")
    @Transactional
    public Object addRole(@RequestBody @Validated RoleForm roleForm) {
        roleForm.setId(null);
        sysRoleService.addRole(roleForm);
        return SUCCESS;
    }

    @RequiresPermissions("role:view")
    @GetMapping("/roles")
    public Object getRoles() {
        return AppResponse.ok(sysRoleService.getRoleList(ApiPageFactory.getPage()));
    }

    @RequiresPermissions("role:view")
    @GetMapping("/roles/all")
    public Object getAllRoles() {
        return AppResponse.ok(sysRoleService.list());
    }

    @RequiresPermissions("role:view")
    @GetMapping("/role/{roleId}")
    public Object getRoleDetail(@PathVariable Long roleId) {
        return BAD_REQUEST;
    }

    @RequiresPermissions("role:view")
    @GetMapping("/role/{roleId}/menu_ids")
    public Object getRoleMenus(@PathVariable Long roleId) {
        final Set<Long> menuIds =
                sysMenuService.findRoleMenus(roleId).stream()
                        // 这里为了适配Vue Tree组件，需要过滤掉顶级菜单
                        .filter(menu -> !"Layout".equals(menu.getComponent()))
                        .map(Menu::getId).collect(Collectors.toSet());
        return AppResponse.ok(menuIds);
    }

    @RequiresPermissions("role:update")
    @PutMapping("/role/{roleId}")
    public Object updateRole(@PathVariable Long roleId, @RequestBody @Validated RoleForm roleForm) {
        roleForm.setId(roleId);
        final boolean b = sysRoleService.updateRole(roleForm);
        return b ? SUCCESS : AppResponse.badRequest(AppMessage.ERROR_OP_FAIL);
    }

    @RequiresPermissions("role:delete")
    @DeleteMapping("/role/{roleId}")
    public Object deleteRole(@PathVariable Long roleId) {
        AssertUtil.isTrue(roleId > 0, ERR_BUILT_IN_ROLE);
        sysRoleService.deleteRole(roleId);
        return SUCCESS;
    }
}
