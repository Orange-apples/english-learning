package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.model.system.User;
import com.cxylm.springboot.systemdto.SysUserDto;

public interface SysUserMapper extends BaseMapper<User> {
    IPage<SysUserDto> getUserList(Page<SysUserDto> page);
}
