package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.BookInfoMapper;
import com.cxylm.springboot.dto.form.CourseSelectionForm;
import com.cxylm.springboot.dto.result.BooksDto;
import com.cxylm.springboot.model.BookInfo;
import com.cxylm.springboot.service.BookInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 课本管理
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BookInfoManageServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo> implements BookInfoService {

    /**
     * 根据条件查询课程
     *
     * @param page
     * @param form
     * @return
     */
    @Override
    public Page<BookInfo> selectBooksByCondition(Page<BookInfo> page, CourseSelectionForm form) {
        List<BookInfo> books = baseMapper.selectBooksByCondition(page, form);
        page.setRecords(books);
        return page;
    }

    @Override
    public List<String> selectBookPublishers() {
        return baseMapper.selectBookPublishers();
    }
}
