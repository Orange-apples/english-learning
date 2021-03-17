package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.result.LoginRecordDto;
import com.cxylm.springboot.model.LoginRecord;

import java.util.Date;
import java.util.List;

/**
 * @Author: shiyanru
 * @Date: 2020/3/9 17:11
 */

public interface LoginRecordService extends IService<LoginRecord> {

    /**
     * 用户访问记录
     * @param userId
     * @return
     */
    Page<LoginRecordDto> selectRecordDetail(Page<LoginRecordDto> page,Integer userId);

    /**
     * 今日使用
     * @return
     */
    Integer selectTodayUse(Integer schoolUserId);

    /**
     * 使用记录
     * @return
     */
    Page<LoginRecordDto> selectRecordList(Page<LoginRecordDto> page);

    /**
     * 添加访问记录
     * @param userId
     */
    void addLoginRecord(Integer userId, Integer schoolId);

    Date getLastLoginTime(Integer userId);
}
