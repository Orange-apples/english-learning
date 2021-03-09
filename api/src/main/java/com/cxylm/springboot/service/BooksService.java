package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.form.CourseSelectionForm;
import com.cxylm.springboot.dto.result.BooksDto;
import com.cxylm.springboot.enums.BookLevel;
import com.cxylm.springboot.model.BookInfo;

import java.util.List;

/**
 * 课本相关
 */
public interface BooksService extends IService<BookInfo> {

    /**
     * 根据阶段获取版本列表
     *
     * @param bookLevel
     */
    List<String> selectEditionByLevel(BookLevel bookLevel);

    /**
     * 根据条件查询课程
     *
     * @param page
     * @param form
     * @return
     */
    Page<BooksDto> selectBooksByCondition(Page<BooksDto> page, CourseSelectionForm form);

    List<String> selectBookPublishers();
}
