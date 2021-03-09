package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.BDStaDto;
import com.cxylm.springboot.model.BD;

import java.util.Date;

public interface SysBDService extends IService<BD> {
    BDStaDto monthSta(Integer bdId, Date date);

    BDStaDto yearSta(Integer bdId, Date date);
}
