package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.form.WordsTestSaveForm;
import com.cxylm.springboot.dto.result.TestResultDto;
import com.cxylm.springboot.dto.result.XyDateDto;
import com.cxylm.springboot.model.StudyTestRecords;

import java.util.List;
import java.util.Map;

/**
 * 测试记录
 */
public interface StudyTestRecordsService extends IService<StudyTestRecords> {
    /**
     * 保存测试记录
     *
     * @param form
     */
    void saveTestRecords(WordsTestSaveForm form,Integer userId);

    /**
     * 柱状图数据
     *
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
    Page<TestResultDto> getTestResult(Page<TestResultDto> page, Integer userId);

    /**
     * 获取对比测试分数
     *
     * @param bookId
     * @param unitId
     * @param userId
     * @return
     */
    Map unitScore(Integer bookId, Integer unitId, Integer userId);
}
