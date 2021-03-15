package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.SysConfigMapper;
import com.cxylm.springboot.model.SysConfig;
import com.cxylm.springboot.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @auther Orange-apples
 * @date 2021/3/15 21:57
 */
@Service
@Slf4j
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {
    @Override
    public String getConfig(String name) {
        SysConfig one = getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getKeyName, name));
        return one == null ? null : one.getKeyValue();
    }
}
