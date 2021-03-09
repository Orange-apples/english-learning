package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.BooksMapper;
import com.cxylm.springboot.dto.form.CourseSelectionForm;
import com.cxylm.springboot.dto.result.BooksDto;
import com.cxylm.springboot.enums.BookLevel;
import com.cxylm.springboot.model.BookInfo;
import com.cxylm.springboot.service.BooksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BooksServiceImpl extends ServiceImpl<BooksMapper, BookInfo> implements BooksService {

    /**
     * 根据阶段获取版本列表
     *
     * @param bookLevel
     */
    @Override
    public List<String> selectEditionByLevel(BookLevel bookLevel) {
        return baseMapper.selectEditionByLevel(bookLevel);
    }

    /**
     * 根据条件查询课程
     *
     * @param page
     * @param form
     * @return
     */
    @Override
    public Page<BooksDto> selectBooksByCondition(Page<BooksDto> page, CourseSelectionForm form) {
        List<BooksDto> books = baseMapper.selectBooksByCondition(page, form);
        page.setRecords(books);
        return page;
    }

    @Override
    public List<String> selectBookPublishers() {
        return baseMapper.selectBookPublishers();
    }
}
