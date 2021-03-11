package com.cxylm.springboot.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.StudyWordRecordsMapper;
import com.cxylm.springboot.dto.form.StudyForm;
import com.cxylm.springboot.dto.form.StudySaveForm;
import com.cxylm.springboot.dto.form.StudySaveWordsForm;
import com.cxylm.springboot.dto.result.WordsDto;
import com.cxylm.springboot.dto.wordRecord.StudentTestRecordDto;
import com.cxylm.springboot.enums.MemoryLevel;
import com.cxylm.springboot.enums.StudyRateState;
import com.cxylm.springboot.enums.WordBookType;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.AppUserConfig;
import com.cxylm.springboot.model.StudyBookRate;
import com.cxylm.springboot.model.StudyWordRecords;
import com.cxylm.springboot.service.AppUserConfigService;
import com.cxylm.springboot.service.StudyWordRecordsService;
import com.cxylm.springboot.service.WordsService;
import com.cxylm.springboot.util.StudyLevelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 已学单词记录
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StudyWordRecordsServiceImpl extends ServiceImpl<StudyWordRecordsMapper, StudyWordRecords> implements StudyWordRecordsService {

    private final WordsService wordsService;
    private final AppUserConfigService appUserConfigService;

    /**
     * 复习单词
     *
     * @param form
     * @param userId
     * @return
     */
    @Override
    public List<WordsDto> review(StudyForm form, Integer userId) {
        AppUserConfig userConfig = appUserConfigService.getById(userId);
        Integer maxReviewWord = null;
        if (userConfig != null) {
            maxReviewWord = userConfig.getMaxReviewWord();
        }
        return baseMapper.review(form, userId, maxReviewWord == null ? 20 : maxReviewWord);
    }

    /**
     * 学习单词记录保存
     *
     * @param form
     * @param userId
     * @param studyBookRate
     */
    @Override
    public void studySave(StudySaveForm form, Integer userId, StudyBookRate studyBookRate) {
        List<StudySaveWordsForm> words = form.getWords();
        Date now = new Date();
        if (studyBookRate != null && (studyBookRate.getState().equals(StudyRateState.LEARNING) || studyBookRate.getState().equals(StudyRateState.TEST_AND_LEARNING))) {
            //校验是否为整个单元提交
//            Long tatle = wordsService.selectTotaleByUnit(form.getBookId(), form.getUnitId());
//            if (tatle != form.getWords().size()) {
//                log.error("该单元未完全学习完，不能保存！");
//                throw new AppBizException("学习未完成，保存失败！");
//            }

            Integer wordsId = 0;
            List<Integer> wordsList = new ArrayList<>();
            for (int i = 0; i < words.size(); i++) {
                Integer wordId = words.get(i).getWordId();
                if (wordId > wordsId) {
                    wordsId = wordId;
                }
                wordsList.add(wordId);
            }
            //校验重复提交单词
            List<StudyWordRecords> uselessWordsList = baseMapper.selectByWordsList(wordsList);
            if (uselessWordsList != null) {
                //删除前端传过来的重复单词
                for (int j = 0; j < uselessWordsList.size(); j++) {
                    for (int i = 0; i < words.size(); i++) {
                        StudySaveWordsForm wordsForm = words.get(i);
                        if (wordsForm.getWordId().equals(uselessWordsList.get(j).getWordId())) {
                            words.remove(wordsForm);
                        }
                    }
                }
            }

            studyBookRate.setLastWordsId(wordsId);

            //如果课程记录为空或状态为学习中，创建单词记录
            boolean b = baseMapper.insertBatch(words, userId, now);
            if (!b) {
                log.error("系统异常！错误码:1001(批量添加单词记录出错)");
                throw new AppBizException("学习保存失败");
            }
        } else {
            //复习，更新单词学习记录
//            List<Integer> list = words.stream().map(StudySaveWordsForm::getWordId).collect(Collectors.toList());
//            List<StudyWordRecords> studyWordRecords = baseMapper.selectBatchIds(list);
            List<StudyWordRecords> studyWordRecords = baseMapper.selectBatchByWordsId(words, userId);
            for (int i = 0; i < words.size(); i++) {
                Integer errorTimes = words.get(i).getErrorTimes();
                StudyWordRecords studyWordRecord = studyWordRecords.get(i);
                MemoryLevel level;
                if (errorTimes > 0) {
                    level = MemoryLevel.HOUR;
                    studyWordRecord.setErrorTimes(studyWordRecord.getErrorTimes() + errorTimes);
                    //最后一次测试时间
                    studyWordRecord.setMemoryTime(now);
                } else {
                    level = StudyLevelUtil.getLevel(now, studyWordRecord.getMemoryTime());
                    studyWordRecord.setMemoryTime(now);
                }
                studyWordRecord.setLevel(level);
            }
            boolean b = updateBatchById(studyWordRecords);
            if (!b) {
                log.error("系统异常！错误码:1002(更新单词学习记录异常)");
                throw new AppBizException("学习保存失败");
            }
        }
    }

    /**
     * 智能听写：听写近期拼写出错频率较高的单词！
     *
     * @param form
     * @param userId
     * @return
     */
    @Override
    public List<WordsDto> dictation(StudyForm form, Integer userId) {
        //查找一个月内错误多的单词
        DateTime lastMonth = DateUtil.lastMonth();
        return baseMapper.dictation(form, userId, lastMonth);
    }

    /**
     * 1汉译英，2英译汉，3听选，4听写,修改测试次数
     *
     * @param words
     * @param userId
     */
    @Override
    public void updateTestTime(List words, Integer userId) {
        baseMapper.updateTestTime(words, userId);
    }

    /**
     * 获取单词本
     *
     * @param page
     * @param userId
     * @param type
     * @return
     */
    @Override
    public Page<WordsDto> getWordBook(Page<WordsDto> page, Integer userId, WordBookType type) {
        List<WordsDto> words = baseMapper.getWordBook(page, userId, type.getValue());
        page.setRecords(words);
        return page;
    }

    /**
     * 是否需要复习
     *
     * @param userId
     * @return
     */
    @Override
    public Integer checkReview(Integer userId) {
        return baseMapper.checkReview(userId);
    }

    @Override
    public Page<StudentTestRecordDto> studentTestRecord(Page<Object> page, Integer id) {
        return baseMapper.studentTestRecord(page, id);
    }
}
