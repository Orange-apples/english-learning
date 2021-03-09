package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.AppUserConfigMapper;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.AppUserConfig;
import com.cxylm.springboot.service.AppUserConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户系统设置
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AppUserConfigServiceImpl extends ServiceImpl<AppUserConfigMapper, AppUserConfig> implements AppUserConfigService {

    /**
     * 初始化用户系统设置
     * @param userId
     * @return
     */
    @Override
    public boolean initAppUserConfig(Integer userId) {
        AppUserConfig appUserConfig = new AppUserConfig();
        appUserConfig.setUserId(userId);
        boolean b = appUserConfig.insert();
        if(!b){
            log.error("初始化用户系统设置失败！");
            throw new AppBizException("初始化设置失败");
        }
        return b;
    }

    /**
     * 更新用户系统设置
     * @param appUserConfig
     * @return
     */
    @Override
    public boolean updateAppUserConfig(AppUserConfig appUserConfig) {
        boolean b = appUserConfig.updateById();
        if(!b){
            log.error("更新用户系统设置失败！");
            throw new AppBizException("设置失败");
        }
        return b;
    }
}
