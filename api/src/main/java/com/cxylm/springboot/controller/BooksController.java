package com.cxylm.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.dto.bookrate.BookRateDto;
import com.cxylm.springboot.dto.category.OpenCourseDto;
import com.cxylm.springboot.dto.form.CourseSelectionForm;
import com.cxylm.springboot.dto.result.BooksDto;
import com.cxylm.springboot.dto.result.BooksUnitStateDto;
import com.cxylm.springboot.dto.result.MyBooksDto;
import com.cxylm.springboot.enums.BookLevel;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.BookInfo;
import com.cxylm.springboot.model.CourseCard;
import com.cxylm.springboot.model.CourseOpenRecord;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.*;
import com.cxylm.springboot.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/b=s")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BooksController extends ApiController {

    private final BooksService booksService;
    private final StudyBookRateService studyBookRateService;
    private final CourseCardService courseCardService;
    private final CourseCardBindService courseCardBindServiceService;
    private final AppUserService appUserService;
    private final RedissonClient redisson;


    @PublicAPI
    @GetMapping("/publishers")
    public Object selectBookPublishers() {
        return AppResponse.ok(booksService.selectBookPublishers());
    }

    /**
     * 根据阶段获取版本
     *
     * @return
     */
    @PublicAPI
    @GetMapping("/getEditionByLevel")
    @Transactional
    public Object getEditionByLevel(@RequestParam Integer level) {
        BookLevel bookLevel = BookLevel.valueOfInt(level);
        if (bookLevel == null || BookLevel.ERROR.equals(bookLevel)) {
            throw new AppBadRequestException("请求错误！");
        }
        List<String> data = booksService.selectEditionByLevel(bookLevel);
        Map<String, List<String>> map = new HashMap<>(2);
        map.put("editionLists", data);
        return AppResponse.ok(map);
    }

    /**
     * 选课
     *
     * @return
     */
//    @PublicAPI
    @GetMapping("/courseSelection")
    public Object courseSelection(@Validated CourseSelectionForm form) {
//        Page<BooksDto> books = booksService.selectBooksByCondition(ApiPageFactory.getPage(), form);
//        return AppResponse.ok(books);
        Page<BooksDto> books = booksService.selectBooksByCondition(ApiPageFactory.getPage(), form);

        //获取试用课程数量
        Integer userId = getUserId();
        int tryCourseCount = studyBookRateService.getTryCourseCount(userId);
        JSONObject returnJson = (JSONObject) JSON.toJSON(books);
        returnJson.put("tryCourseCount",tryCourseCount);
        return AppResponse.ok(returnJson);
    }

    /**
     * 用户选课界面
     * @param form
     * @return
     */
    @GetMapping("/userCourseSelection")
    public Object userCourseSelection(@Validated CourseSelectionForm form) {
        Page<BooksDto> books = booksService.selectBooksByCondition(ApiPageFactory.getPage(), form);

        //获取试用课程数量
        Integer userId = getUserId();
        int tryCourseCount = studyBookRateService.getTryCourseCount(userId);
        JSONObject returnJson = (JSONObject) JSON.toJSON(books);
        returnJson.put("tryCourseCount",tryCourseCount);
        return AppResponse.ok(returnJson);
    }

    @PostMapping("/{bookId}/open-free")
    public Object OpenFreeCourse(@PathVariable Integer bookId) {
        final Integer userId = getUserId();
        final RLock lock = redisson.getLock(CacheName.COURSE_LOCK + userId);
        try {
            final boolean isLocked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!isLocked) {
                return AppResponse.badRequest("服务器繁忙，请稍后再试");
            }
            studyBookRateService.openFreeCourse(bookId, userId);
        } catch (InterruptedException e) {
            log.error("Error while getting COURSE_LOCK redis lock.", e);
            return AppResponse.badRequest("服务器繁忙，请稍后再试");
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
        return SUCCESS;
    }

    /**
     * 开通课程
     */
    @PostMapping("/open")
    @Transactional
    public Object OpenCourse(@RequestBody @Validated OpenCourseDto form) {
        CourseCard courseCard = courseCardService.getOne(new QueryWrapper<CourseCard>().eq("no", form.getNo()).eq("password", form.getPassword()));
        AssertUtil.isTrue(courseCard != null && !courseCard.getUsed(), "无效的卡密");
        final Long expireTime = courseCard.getExpireTime();
        if (expireTime != null) AssertUtil.isTrue(expireTime > System.currentTimeMillis(), "该卡已过期");


        Integer userId = getUserId();
//        Integer bookId = courseCard.getCourseId();
        Integer bookId = form.getBookId();

        BookInfo bookInfo = booksService.getById(bookId);
        AssertUtil.isTrue(bookInfo != null, "课程不存在，请联系平台");

        AssertUtil.isTrue(courseCardBindServiceService.isBind(bookId, courseCard.getId()), "无效的卡密");
        //if (courseCard.getCourseLevel() != null) AssertUtil.isTrue(courseCard.getCourseLevel().equals(bookInfo.getLevel().getValue()), "无效的卡密");
        //if (courseCard.getCoursePublisher() != null) AssertUtil.isTrue(courseCard.getCoursePublisher().equals(bookInfo.getEdition()), "无效的卡密");

        final RLock lock = redisson.getLock(CacheName.COURSE_LOCK + userId);
        try {
            final boolean isLocked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!isLocked) {
                return AppResponse.badRequest("服务器繁忙，请稍后再试");
            }
            boolean state = studyBookRateService.checkOpenState(bookId, userId,0);
            if (state) {
                throw new AppBadRequestException("该课程已经开通了");
            }
            final boolean bounded = courseCardBindServiceService.bindUser(userId, bookId, courseCard.getId());
            if (!bounded) return AppResponse.badRequest("该课程已被他人开通了");

            studyBookRateService.OpenCourse(bookId, userId, expireTime);

            AppUser appUser = appUserService.getById(getUserId());
            if (appUser != null && appUser.getBdCode() != null) {
                CourseOpenRecord record = new CourseOpenRecord();
                record.setBdId(appUser.getBdCode());
                record.setCourseId(bookId);
                record.setUserId(userId);
                record.setCoursePrice(bookInfo.getPrice());
                record.insert();
            }

            courseCard.setUsed(true);
            //courseCard.updateById();
            //将卡券绑定的业务员绑定到用户上
            courseCardService.bindBdToUser(courseCard.getId(),userId);
            courseCardService.incUseCount(courseCard.getId());
        } catch (InterruptedException e) {
            log.error("Error while getting COURSE_LOCK redis lock.", e);
            return AppResponse.badRequest("服务器繁忙，请稍后再试");
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }

        return SUCCESS;
    }

    /**
     * 获取课程
     *
     * @return
     */
    @GetMapping("/myBooks")
    @Transactional
    public Object getMyBooks() {
        Integer userId = getUserId();
//        List<MyBooksDto> myBooks = studyBookRateService.getMyBooks(userId);
        List<MyBooksDto> usableBooks = studyBookRateService.getBooksByStatus(userId, 1);
        List<MyBooksDto> expireBooks = studyBookRateService.getBooksByStatus(userId, 2);
        Map<String, List<MyBooksDto>> map = new HashMap<>(2);
//        map.put("myBooks", myBooks);
        map.put("usableBooks", usableBooks);
        map.put("expireBooks", expireBooks);
        return AppResponse.ok(map);
    }

    /**
     * 单词基础
     *
     * @return
     */
    @GetMapping("/base")
    @Transactional
    public Object base(@RequestParam Integer bookId) {
        List<BooksUnitStateDto> base = studyBookRateService.getBookUnitState(bookId, getUserId());
        Map<String, List<BooksUnitStateDto>> map = new HashMap<>(2);
        map.put("base", base);
        return AppResponse.ok(map);
    }

    /**
     * 根据id查询课程详情
     *
     * @param bookId
     * @return
     */
    @PublicAPI
    @GetMapping("/{bookId}")
    public Object selectBookDetail(@PathVariable Integer bookId) {
        BookInfo book = booksService.getById(bookId);
        Map<String, BookInfo> map = new HashMap<>(2);
        map.put("book", book);
        return AppResponse.ok(map);
    }

    /**
     * 提前开通课程
     * @return
     */
    @PostMapping("/buyUnit")
    public Object buyUnit(@RequestBody BookRateDto bookRateDto){
        Integer userId = getUserId();
        return studyBookRateService.buyUnit(bookRateDto.getBookId(),userId,bookRateDto.getUnitId());
    }
}
