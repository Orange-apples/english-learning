package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.result.AppUpdateDto;
import com.cxylm.springboot.model.AppUpdate;

public interface AppUpdateService extends IService<AppUpdate> {
    AppUpdateDto checkForUpdate(int platform);
}
