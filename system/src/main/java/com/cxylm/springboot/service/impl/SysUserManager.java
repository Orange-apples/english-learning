package com.cxylm.springboot.service.impl;

import com.cxylm.springboot.model.system.User;
import com.cxylm.springboot.service.SysUserService;
import com.cxylm.springboot.util.RedisCacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SysUserManager {
    private final RedisCacheUtil redisCacheUtil;
    private final SysUserService sysUserService;

    public User findByUserName(String username) {
        return redisCacheUtil.selectCacheByTemplate(
                () -> (User) this.redisCacheUtil.get(username),
                () -> this.sysUserService.findByUserName(username));
    }
}
