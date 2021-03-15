package com.cxylm.springboot.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.model.SysConfig;

public interface SysConfigService extends IService<SysConfig> {
    String getConfig(String name);

    void set(String jsonStr);

    Object get();
}
