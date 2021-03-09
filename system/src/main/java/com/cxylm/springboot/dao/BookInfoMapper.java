package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.form.CourseSelectionForm;
import com.cxylm.springboot.model.BookInfo;

import java.util.List;

/**
 * 课本管理
 */
public interface BookInfoMapper extends BaseMapper<BookInfo> {
    List<BookInfo> selectBooksByCondition(Page<BookInfo> page, CourseSelectionForm form);

    List<String> selectBookPublishers();
}
