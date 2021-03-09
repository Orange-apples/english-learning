package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.service.AppUpdateService;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.dao.AppUpdateMapper;
import com.cxylm.springboot.dto.result.AppUpdateDto;
import com.cxylm.springboot.model.AppUpdate;
import com.cxylm.springboot.util.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUpdateServiceImpl extends ServiceImpl<AppUpdateMapper, AppUpdate> implements AppUpdateService {
    private final RedisCacheUtil cacheUtil;

    @Autowired
    public AppUpdateServiceImpl(RedisCacheUtil cacheUtil) {
        this.cacheUtil = cacheUtil;
    }

    @Override
    public AppUpdateDto checkForUpdate(int platform) {
        AppUpdateDto res = (AppUpdateDto) cacheUtil.get(CacheName.APP_UPDATE);
        if (res == null) {
            res = baseMapper.checkForUpdate(platform);
            cacheUtil.set(CacheName.APP_UPDATE, String.valueOf(platform), res);
        }
        return res;
    }
}
