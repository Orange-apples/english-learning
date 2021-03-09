package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.form.WordsSelectionForm;
import com.cxylm.springboot.model.Words;

import java.util.List;

/**
 * 词库管理
 * @author HaoTi
 */
public interface WordsManageMapper extends BaseMapper<Words> {

    /**
     * 获取课程单词
     *
     * @param page
     * @param bookId
     * @param form
     * @return
     */
    List<Words> selectAllWordsByCourse(Page<Words> page, Integer bookId, WordsSelectionForm form);
}
