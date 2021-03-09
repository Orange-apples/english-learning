package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.SysRoleMenuMapper;
import com.cxylm.springboot.model.system.RoleMenu;
import com.cxylm.springboot.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, RoleMenu> implements SysRoleMenuService {

}
