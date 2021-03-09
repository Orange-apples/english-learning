package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.form.StudyForm;
import com.cxylm.springboot.dto.form.WordsTestForm;
import com.cxylm.springboot.dto.result.TestWordsDto;
import com.cxylm.springboot.dto.result.WordsDto;
import com.cxylm.springboot.model.StudyBookRate;
import com.cxylm.springboot.model.Words;

import java.util.List;
import java.util.Map;

/**
 * 单词基础
 */
public interface WordsService extends IService<Words> {

    /**
     * 获取学习单词
     *
     * @param form
     * @param studyBookRate
     * @return
     */
    List<WordsDto> getStudyWords(StudyForm form, StudyBookRate studyBookRate);

    /**
     * 获取1汉译英，2英译汉，3听选，4听写单词
     *
     * @param form
     * @param userId
     * @return
     */
    Map<String, List<TestWordsDto>> getTestWords(WordsTestForm form, Integer userId);

    /**
     * 获取5单元测试，6一测到底，7学前测试，8学后测试
     *
     * @param form
     * @param userId
     * @return
     */
    Map<String, List<TestWordsDto>> getAllTestWords(Page<TestWordsDto> page, WordsTestForm form, Integer userId);

    /**
     * 添加释义
     * @param wordsDtos
     */
    void addSymbol(List<WordsDto> wordsDtos);

    /**
     * 查询本单元单词数量
     * @param bookId
     * @param unitId
     * @return
     */
    Long selectTotaleByUnit(Integer bookId, Integer unitId);

    /**
     * 获取总数，一测到底
     * @param bookId
     * @return
     */
    Long getTotalByBookId(Integer bookId);

    int getMaxUnit(Integer bookId);

    Integer getMaxWordIdByUnit(Integer bookId, Integer unitId);
}
