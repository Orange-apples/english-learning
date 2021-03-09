package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.form.WordsSelectionForm;
import com.cxylm.springboot.model.BookInfo;
import com.cxylm.springboot.model.Words;
import org.springframework.web.multipart.MultipartFile;

/**
 * 词库管理
 */
public interface WordsManageService extends IService<Words> {

    /**
     * 批量导入词库
     * @param fileName  文件名
     * @param file
     * @param bookInfo
     * @return
     */
    boolean batchImport(String fileName, MultipartFile file, BookInfo bookInfo);

    /**
     * 获取课程单词
     * @param page
     * @param bookId
     * @param form
     * @return
     */
    Page<Words> getAllWordsByCourse(Page<Words> page, Integer bookId, WordsSelectionForm form);
}
