package com.cxylm.springboot.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.StudyTestRecordsMapper;
import com.cxylm.springboot.dto.form.WordsTestSaveForm;
import com.cxylm.springboot.dto.result.TestResultDto;
import com.cxylm.springboot.dto.result.XyDateDto;
import com.cxylm.springboot.enums.TestType;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.StudyTestRecords;
import com.cxylm.springboot.model.Words;
import com.cxylm.springboot.service.StudyTestRecordsService;
import com.cxylm.springboot.service.WordsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 测试记录
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StudyTestRecordsServiceImpl extends ServiceImpl<StudyTestRecordsMapper, StudyTestRecords> implements StudyTestRecordsService {

    private final WordsService wordsService;

    /**
     * 保存测试记录
     *
     * @param form
     */
    @Override
    public void saveTestRecords(WordsTestSaveForm form, Integer userId) {
        StudyTestRecords studyTestRecords = new StudyTestRecords();
        BeanUtils.copyProperties(form, studyTestRecords);
        studyTestRecords.setCreateTime(new Date());
        studyTestRecords.setUserId(userId);
        if (form.getAutoTestType() != null) {
            studyTestRecords.setTestType(form.getAutoTestType().get(0));
        }
        boolean b = studyTestRecords.insert();
        if (!b) {
            throw new AppBizException("保存测试记录失败！");
        }
    }

    /**
     * 柱状图数据
     *
     * @param userId
     * @return
     */
    @Override
    public List<XyDateDto> histogram(Integer userId) {
        List<XyDateDto> histogram = baseMapper.histogram(userId);
        Date date = new Date();
        if (histogram == null) {
            histogram = new ArrayList<>(16);
            for (int i = 1; i < 7; i++) {
                XyDateDto xyDateDto = new XyDateDto();
                xyDateDto.setXData(DateUtil.format(DateUtil.offsetDay(date, -i), DatePattern.NORM_DATE_PATTERN));
                xyDateDto.setYData(0);
                histogram.add(xyDateDto);
            }
        }
        return histogram;
    }

    /**
     * 测试列表
     *
     * @param page
     * @param userId
     * @return
     */
    @Override
    public Page<TestResultDto> getTestResult(Page<TestResultDto> page, Integer userId) {
        List<TestResultDto> testResult = baseMapper.getTestResult(page, userId);

        //查询对应的测试单元下的单词列表
        testResult.forEach(r -> {
            Integer value = r.getTestType().getValue();
            List<Integer> ints = Arrays.asList(1, 2, 3, 4);
            if (ints.contains(value)) {
                //普通测试
                r.setNormalTest(1);
                r.setWordCount(10);
            } else {
                LambdaQueryWrapper<Words> queryWrapper = new LambdaQueryWrapper<Words>()
                        .eq(Words::getBookId, r.getBookId())
                        .eq(Words::getUnit, r.getUnitId());
                int wordCount = wordsService.count(queryWrapper);
                r.setWordCount(wordCount);
            }
        });
        page.setRecords(testResult);
        return page;
    }


    /**
     * 获取对比测试分数
     *
     * @param bookId
     * @param unitId
     * @param userId
     * @return
     */
    @Override
    public Map unitScore(Integer bookId, Integer unitId, Integer userId) {
        Integer before = baseMapper.unitScore(bookId, unitId, userId, TestType.BEFORE_TEST.value());
        Integer after = baseMapper.unitScore(bookId, unitId, userId, TestType.AFTER_TEST.value());

        Map<String, Integer> map = new HashMap<>(4);
        map.put("beforeScore", before);
        map.put("afterScore", after);
        return map;
    }
}
