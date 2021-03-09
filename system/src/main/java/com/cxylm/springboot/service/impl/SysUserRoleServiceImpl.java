package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.SysUserRoleMapper;
import com.cxylm.springboot.model.system.UserRole;
import com.cxylm.springboot.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, UserRole> implements SysUserRoleService {

}
