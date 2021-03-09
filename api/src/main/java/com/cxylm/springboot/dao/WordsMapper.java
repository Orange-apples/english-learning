package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.form.StudyForm;
import com.cxylm.springboot.dto.form.WordsTestForm;
import com.cxylm.springboot.dto.result.TestWordsDto;
import com.cxylm.springboot.dto.result.WordsDto;
import com.cxylm.springboot.model.Words;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单词基础
 * @author HaoTi
 */
public interface WordsMapper extends BaseMapper<Words> {

    /**
     * 获取学习单词
     *
     * @param form
     * @param lastWordsId
     * @return
     */
    List<WordsDto> getStudyWords(@Param("form") StudyForm form, @Param("lastWordsId") Integer lastWordsId);

    /**
     * 获取测试单词 before
     * @param form
     * @param userId
     * @return
     */
    List<TestWordsDto> getTestWords(WordsTestForm form, Integer userId);

    /**
     * 获取测试单词
     * @param size
     * @param userId
     * @return
     */
//    List<TestWordsDto> getTestWord(@Param("limit_size") Integer size, @Param("userId") Integer userId);

    /**
     * 获取混淆词汇
     * 默认获取20个混淆词
     *
     * 意图：从一本书中取混淆词，一次只取20个，可以保证一定能取到20个混淆词
     * @param bookId
     * @param size
     * @return
     */
    List<WordsDto> getBlurWords(Integer bookId, int size);

    /**
     * 获取单元测试单词
     * @param form
     * @return
     */
    List<TestWordsDto> getUnitTestWords(WordsTestForm form);

    /**
     * 获取一测到底单词
     *
     * @param page
     * @param bookId
     * @param userId
     * @return
     */
    List<TestWordsDto> getUntilTestWords(Page<TestWordsDto> page, Integer bookId, Integer userId);

    /**
     * 查询本单元单词数量
     *
     * @param bookId
     * @param unitId
     * @return
     */
    Long selectTotaleByUnit(Integer bookId, Integer unitId);

    /**
     * 获取总数，一测到底
     *
     * @param bookId
     * @return
     */
    Long getTotalByBookId(Integer bookId);

    int getMaxUnit(Integer bookId);

    Integer getMaxWordIdByUnit(@Param("bookId") Integer bookId, @Param("unitId") Integer unitId);
}
