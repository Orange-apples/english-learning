package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxylm.springboot.dto.result.AppUpdateDto;
import com.cxylm.springboot.model.AppUpdate;
import org.apache.ibatis.annotations.Param;


public interface AppUpdateMapper extends BaseMapper<AppUpdate> {
    AppUpdateDto checkForUpdate(@Param("platform") int platform);
}
