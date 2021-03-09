package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxylm.springboot.dto.BDStaDto;
import com.cxylm.springboot.model.CourseOpenRecord;

import java.util.Date;

public interface CourseOpenRecordMapper extends BaseMapper<CourseOpenRecord> {
    BDStaDto sta(Integer bdId, Date begin, Date end);
}
