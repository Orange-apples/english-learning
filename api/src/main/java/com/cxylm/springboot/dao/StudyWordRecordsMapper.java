package com.cxylm.springboot.dao;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.form.StudyForm;
import com.cxylm.springboot.dto.form.StudySaveWordsForm;
import com.cxylm.springboot.dto.result.WordsDto;
import com.cxylm.springboot.model.StudyWordRecords;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 已学单词记录
 * @author HaoTi
 */
public interface StudyWordRecordsMapper extends BaseMapper<StudyWordRecords> {

    /**
     * 复习单词
     *
     * @param form
     * @param userId
     * @param maxReviewWord
     * @return
     */
    List<WordsDto> review(StudyForm form, Integer userId, Integer maxReviewWord);

    /**
     * 批量添加单词记录
     * @param words
     * @param userId
     * @param memoryTime
     * @return
     */
    boolean insertBatch(List<StudySaveWordsForm> words, Integer userId, Date memoryTime);

    /**
     * 智能听写：听写近期拼写出错频率较高的单词！
     * @param form
     * @param userId
     * @param lastMonth
     * @return
     */
    List<WordsDto> dictation(StudyForm form, Integer userId, DateTime lastMonth);

    /**
     * 1汉译英，2英译汉，3听选，4听写,修改测试次数
     *
     * @param words
     * @param userId
     */
    boolean updateTestTime(List words, Integer userId);

    /**
     * 获取单词本
     *
     * @param page
     * @param userId
     * @param type
     * @return
     */
    List<WordsDto> getWordBook(Page<WordsDto> page, Integer userId, Integer type);

    /**
     * 根据wordsId查询用户单词记录
     * @param words
     * @param userId
     * @return
     */
    List<StudyWordRecords> selectBatchByWordsId(List<StudySaveWordsForm> words, Integer userId);

    /**
     * 是否需要复习
     * @param userId
     * @return
     */
    Integer checkReview(@Param("userId") Integer userId);

    List<StudyWordRecords> selectByWordsList(@Param("wordsList") List<Integer> wordsList);
}
