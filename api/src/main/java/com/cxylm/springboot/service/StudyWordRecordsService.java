package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.form.StudyForm;
import com.cxylm.springboot.dto.form.StudySaveForm;
import com.cxylm.springboot.dto.result.WordsDto;
import com.cxylm.springboot.enums.WordBookType;
import com.cxylm.springboot.model.StudyBookRate;
import com.cxylm.springboot.model.StudyWordRecords;

import java.util.List;

/**
 * 已学单词记录
 */
public interface StudyWordRecordsService extends IService<StudyWordRecords> {
    /**
     * 复习单词
     *
     * @param form
     * @param userId
     * @return
     */
    List<WordsDto> review(StudyForm form, Integer userId);

    /**
     * 学习单词记录保存
     *
     * @param form
     * @param userId
     * @param studyBookRate
     */
    void studySave(StudySaveForm form, Integer userId, StudyBookRate studyBookRate);

    /**
     * 智能听写：听写近期拼写出错频率较高的单词！
     *
     * @param form
     * @param userId
     * @return
     */
    List<WordsDto> dictation(StudyForm form, Integer userId);

    /**
     * 1汉译英，2英译汉，3听选，4听写,修改测试次数
     *
     * @param words
     * @param userId
     */
    void updateTestTime(List words, Integer userId);

    /**
     * 获取单词本
     *
     * @param page
     * @param userId
     * @param type
     * @return
     */
    Page<WordsDto> getWordBook(Page<WordsDto> page, Integer userId, WordBookType type);

    /**
     * 是否需要复习
     *
     * @param userId
     * @return
     */
    Integer checkReview(Integer userId);
}
