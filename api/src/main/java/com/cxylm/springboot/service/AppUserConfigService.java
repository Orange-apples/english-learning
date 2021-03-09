package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.model.AppUserConfig;

/**
 * 用户系统设置
 */
public interface AppUserConfigService extends IService<AppUserConfig> {

    /**
     * 初始化用户系统设置
     * @param userId
     * @return
     */
    boolean initAppUserConfig(Integer userId);

    /**
     * 更新用户系统设置
     * @param appUserConfig
     * @return
     */
    boolean updateAppUserConfig(AppUserConfig appUserConfig);
}
