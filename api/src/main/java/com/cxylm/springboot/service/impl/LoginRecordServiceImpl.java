package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.LoginRecordMapper;
import com.cxylm.springboot.dto.result.LoginRecordDto;
import com.cxylm.springboot.model.LoginRecord;
import com.cxylm.springboot.service.LoginRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: shiyanru
 * @Date: 2020/3/9 17:56
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginRecordServiceImpl extends ServiceImpl<LoginRecordMapper, LoginRecord> implements LoginRecordService {

    /**
     * 用户访问记录
     *
     * @param userId
     * @return
     */
    public Page<LoginRecordDto> selectRecordDetail(Page<LoginRecordDto> page, Integer userId) {
        List<LoginRecordDto> recordDtoList = baseMapper.selectRecordDetail(page, userId);
        page.setRecords(recordDtoList);
        return page;
    }

    /**
     * 今日使用
     *
     * @return
     */
    public Integer selectTodayUse(Integer schoolUserId) {
        return baseMapper.selectTodayUse(schoolUserId);
    }

    /**
     * 使用记录
     *
     * @return
     */
    public Page<LoginRecordDto> selectRecordList(Page<LoginRecordDto> page) {
        List<LoginRecordDto> useList = baseMapper.selectRecordList(page);
        page.setRecords(useList);
        return page;
    }

    /**
     * 添加访问记录
     *
     * @param userId
     */
    @Transactional
    public void addLoginRecord(Integer userId, Integer schoolId) {
        LoginRecord record = new LoginRecord();
        record.setUserId(userId);
        record.setSchoolId(schoolId);
        record.setLoginTime(new Date());
        record.insert();
    }

    /**
     * 获取最后登录时间
     */
    public Date getLastLoginTime(Integer userId) {
        LoginRecord one = this.getOne(new LambdaQueryWrapper<LoginRecord>()
                .eq(LoginRecord::getUserId, userId)
                .orderByDesc(LoginRecord::getId)
                .last("limit 1"));
        return one.getLoginTime();
    }

}
