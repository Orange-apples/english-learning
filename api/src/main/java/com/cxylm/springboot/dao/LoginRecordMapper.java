package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.result.LoginRecordDto;
import com.cxylm.springboot.model.LoginRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: shiyanru
 * @Date: 2020/3/9 17:57
 */

public interface LoginRecordMapper extends BaseMapper<LoginRecord> {

    /**
     * 用户访问记录
     * @param userId
     * @return
     */
    List<LoginRecordDto> selectRecordDetail(Page<LoginRecordDto> page, @Param("userId") Integer userId);

    /**
     * 今日使用
     * @return
     */
    Integer selectTodayUse(@Param("schoolUserId") Integer schoolUserId);

    /**
     * 使用记录
     * @return
     */
    List<LoginRecordDto> selectRecordList(Page<LoginRecordDto> page);
}
