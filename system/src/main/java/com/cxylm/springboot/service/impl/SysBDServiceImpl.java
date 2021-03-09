package com.cxylm.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.CourseOpenRecordMapper;
import com.cxylm.springboot.dao.SysBDMapper;
import com.cxylm.springboot.dto.BDStaDto;
import com.cxylm.springboot.model.BD;
import com.cxylm.springboot.service.SysBDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SysBDServiceImpl extends ServiceImpl<SysBDMapper, BD> implements SysBDService {
    private final CourseOpenRecordMapper recordMapper;

    @Override
    public BDStaDto monthSta(Integer bdId, Date date) {
        Date begin = DateUtil.beginOfMonth(date);
        Date end = DateUtil.endOfMonth(date);
        return recordMapper.sta(bdId, begin, end);
    }

    @Override
    public BDStaDto yearSta(Integer bdId, Date date) {
        Date begin = DateUtil.beginOfYear(date);
        Date end = DateUtil.endOfYear(date);
        return recordMapper.sta(bdId, begin, end);
    }
}
