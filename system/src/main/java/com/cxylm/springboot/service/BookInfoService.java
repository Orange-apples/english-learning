package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.form.CourseSelectionForm;
import com.cxylm.springboot.model.BookInfo;

import java.util.List;

/**
 * 课本管理
 */
public interface BookInfoService extends IService<BookInfo> {

    Page<BookInfo> selectBooksByCondition(Page<BookInfo> page, CourseSelectionForm form);

    List<String> selectBookPublishers();
}
