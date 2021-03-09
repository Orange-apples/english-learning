package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.result.TestResultDto;
import com.cxylm.springboot.dto.result.XyDateDto;
import com.cxylm.springboot.model.StudyTestRecords;

import java.util.List;

/**
 * 测试记录
 * @author HaoTi
 */
public interface StudyTestRecordsMapper extends BaseMapper<StudyTestRecords> {

    /**
     * 柱状图数据
     * @param userId
     * @return
     */
    List<XyDateDto> histogram(Integer userId);

    /**
     * 测试列表
     *
     * @param page
     * @param userId
     * @return
     */
    List<TestResultDto> getTestResult(Page<TestResultDto> page, Integer userId);

    /**
     * 获取对比测试分数
     *
     * @param bookId
     * @param unitId
     * @param userId
     * @param type
     * @return
     */
    Integer unitScore(Integer bookId, Integer unitId, Integer userId, int type);
}
