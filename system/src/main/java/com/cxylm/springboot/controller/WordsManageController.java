package com.cxylm.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.dto.form.AddWordsForm;
import com.cxylm.springboot.dto.form.CourseSelectionForm;
import com.cxylm.springboot.dto.form.WordsSelectionForm;
import com.cxylm.springboot.enums.BookLevel;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.BookInfo;
import com.cxylm.springboot.model.Words;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.BookInfoService;
import com.cxylm.springboot.service.WordsManageService;
import com.cxylm.springboot.util.ExcelImportUtils;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/manager/words")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WordsManageController extends ApiController {

    private final WordsManageService wordsManageService;
    private final BookInfoService bookInfoService;

    @PostMapping("/add")
    @PublicAPI
    @Transactional
    public Object addWords(@Validated AddWordsForm form) {
        //判断文件是否为空
//        if (file == null) {
//            throw new AppBadRequestException("文件为空!");
//        }
        int count = bookInfoService.count(new LambdaQueryWrapper<BookInfo>().eq(BookInfo::getName, form.getName()));
        if (count > 0) {
            throw new AppBadRequestException("课程名称重复");
        }

        //获取文件名
        MultipartFile file = form.getFile();
        String fileName = file.getOriginalFilename();

        //验证文件名是否合格
        if (!ExcelImportUtils.validateExcel(fileName)) {
            throw new AppBadRequestException("文件必须是excel格式！");
        }
        BookInfo bookInfo = new BookInfo();
        BeanUtils.copyProperties(form, bookInfo, "level");
        BookLevel level = BookLevel.valueOfInt(form.getLevel());
        if (level == null) {
            throw new AppBadRequestException("学习阶段错误！");
        }
        bookInfo.setLevel(level);

        //批量导入
        boolean b = wordsManageService.batchImport(fileName, file, bookInfo);
        return SUCCESS;
    }

    /**
     * 获取所有课程
     * 分页
     *
     * @return
     */
    @PostMapping("/allCourse")
    @RequiresPermissions("word:all")
    @Transactional
    public Object allCourse(@Validated CourseSelectionForm form) {
        Page<BookInfo> bookInfos = bookInfoService.selectBooksByCondition(ApiPageFactory.getPage(), form);
        return AppResponse.ok(bookInfos);
    }

    /**
     * 修改课程信息
     *
     * @return
     */
    @PostMapping("/updateCourse")
    @RequiresPermissions("word:all")
    @Transactional
    public Object updateCourse(@RequestBody @Validated BookInfo bookInfo) {
        boolean b = bookInfoService.updateById(bookInfo);
        if (!b) {
            throw new AppBizException("修改课程信息失败");
        }
        return SUCCESS;
    }

    /**
     * 获取课程单词
     *
     * @return
     */
    @PostMapping("/getWords/{bookId}")
    @RequiresPermissions("word:all")
    @Transactional
    public Object getWords(@PathVariable Integer bookId, @Validated WordsSelectionForm form) {
        Page<Words> words = wordsManageService.getAllWordsByCourse(ApiPageFactory.getPage(), bookId, form);
        return AppResponse.ok(words);
    }

    /**
     * 新增单词
     *
     * @return
     */
    @PostMapping("/addWord")
    @RequiresPermissions("word:all")
    @Transactional
    public Object addWord(@RequestBody @Validated Words word) {
        boolean b = word.insert();
        if (!b) {
            throw new AppBizException("新增单词失败");
        }
        return SUCCESS;
    }

    /**
     * 修改单词
     *
     * @return
     */
    @PostMapping("/updateWord")
    @RequiresPermissions("word:all")
    @Transactional
    public Object updateWord(@RequestBody @Validated Words word) {
        boolean b = word.updateById();
        if (!b) {
            throw new AppBizException("新增单词失败");
        }
        return SUCCESS;
    }

    /**
     * 删除单词
     *
     * @return
     */
    @DeleteMapping("/deleteWord/{wordsId}")
    @RequiresPermissions("word:all")
    @Transactional
    public Object deleteWord(@PathVariable Integer wordsId) {
        boolean b = wordsManageService.removeById(wordsId);
        if (!b) {
            throw new AppBizException("删除单词失败");
        }
        return SUCCESS;
    }
}
