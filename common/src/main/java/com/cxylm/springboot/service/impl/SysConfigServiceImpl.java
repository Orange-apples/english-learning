package com.cxylm.springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.model.SysConfig;
import com.cxylm.springboot.service.SysConfigService;
import com.cxylm.springboot.service.dao.SysConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public SysConfig getSysConfig(String name) {
        return getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getKeyName, name));
    }


    @Override
    public void set(String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        jsonObject.forEach((key, value) -> {
            SysConfig sysConfig = getSysConfig(key);
            if (sysConfig == null) sysConfig = new SysConfig();
            sysConfig.setKeyName(key);
            sysConfig.setKeyValue((String) value);
            saveOrUpdate(sysConfig);
        });
    }

    @Override
    public Object get() {
        List<SysConfig> list = list();
        HashMap<String, String> map = new HashMap<>();
        for (SysConfig sysConfig : list) {
            map.put(sysConfig.getKeyName(), sysConfig.getKeyValue());
        }
        return map;
    }
}
