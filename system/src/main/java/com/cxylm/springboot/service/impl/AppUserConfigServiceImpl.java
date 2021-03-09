package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.AppUserConfigMapper;
import com.cxylm.springboot.model.AppUserConfig;
import com.cxylm.springboot.service.AppUserConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户系统设置
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AppUserConfigServiceImpl extends ServiceImpl<AppUserConfigMapper, AppUserConfig> implements AppUserConfigService {

}
