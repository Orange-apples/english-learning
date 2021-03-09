package com.cxylm.springboot.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.dto.form.*;
import com.cxylm.springboot.dto.result.StudyStateDto;
import com.cxylm.springboot.dto.result.TestWordsDto;
import com.cxylm.springboot.dto.result.WordsDto;
import com.cxylm.springboot.enums.StudyType;
import com.cxylm.springboot.enums.TestType;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.*;
import com.cxylm.springboot.response.AppErrorResponse;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class WordsController extends ApiController {

    private final WordsService wordsService;
    private final AppUserService appUserService;
    private final BooksService booksService;
    private final StudyWordRecordsService studyWordRecordsService;
    private final StudyBookRateService studyBookRateService;
    private final StudyTestRecordsService studyTestRecordsService;
    private final RedissonClient redisson;

    /**
     * 单词首页
     *
     * @return
     */
    @GetMapping
    @Transactional
    public Object getStudyState() {

        AppUser appUser = appUserService.getById(getUserId());

        StudyStateDto studyStateDto = new StudyStateDto();
        BeanUtils.copyProperties(appUser, studyStateDto);

        //查询单词
        Integer lastWordId = appUser.getLastWordId();
        if (lastWordId != null) {
            Words word = wordsService.getById(lastWordId);
            studyStateDto.setLastWord(word.getWord());
        }
        //查询课程名
        Integer bookId = appUser.getBookId();
        if (bookId != null) {
            BookInfo bookInfo = booksService.getById(bookId);
            studyStateDto.setBookName(bookInfo.getName());
        }

        return AppResponse.ok(studyStateDto);
    }

    /**
     * 综合学习
     *
     * @return
     */
    @PostMapping("/study")
    public Object study(@RequestBody @Validated StudyForm form) {
        Integer userId = getUserId();
        final Integer bookId = form.getBookId();
        List<WordsDto> wordsDtos;
        StudyBookRate studyBookRate = null;
        if (form.getStudyType() == null || StudyType.REVIEW.equals(form.getStudyType())) {
            //复习
            wordsDtos = studyWordRecordsService.review(form, userId);
        } else {
            if (bookId == null) {
                //非复习判断bookId和unitId是否为空
                log.error("请求错误：错误码2000(bookId为空)");
                throw new AppBadRequestException("请先进行学习，再进行智能听写");
            }

            final StudyType studyType = form.getStudyType();
            final boolean courseExpired = studyBookRateService.isCourseExpired(bookId, userId);
            if (courseExpired && studyType == StudyType.LEARN) throw new AppBadRequestException("该课程已过期, 当前仅能复习");
            if (courseExpired) {
                final int month = DateUtil.month(DateTime.now());
                if (month != 6 && month != 12) throw new AppBadRequestException("该课程已过期, 仅可在6月及12月进行复习");
            }

            if (form.getUnitId() == null && !StudyType.DICTATION.equals(studyType)) {
                //非复习判断bookId和unitId是否为空
                log.error("请求错误：错误码2000(unitId为空)");
                throw new AppBadRequestException("请先进行单元学习，再进行智能听写");
            }


            final RLock lock = redisson.getLock(CacheName.COURSE_LOCK + userId);
            try {
                final boolean isLocked = lock.tryLock(5, 60, TimeUnit.SECONDS);
                if (!isLocked) {
                    return AppResponse.badRequest("服务器繁忙，请稍后再试");
                }
                if (StudyType.LEARN.equals(form.getStudyType())) {
                    //单词学习,添加学习记录
                    studyBookRate = studyBookRateService.addStudyBookRate(form, userId);
                } else if (StudyType.LISTEN_TEST.equals(form.getStudyType())) {
                    //听读训练，判断是否可以进行听读训练
                    studyBookRateService.check(bookId, form.getUnitId(), userId);
                }
            } catch (InterruptedException e) {
                log.error("Error while getting appuser redis lock.", e);
                return AppResponse.badRequest("服务器繁忙，请稍后再试");
            } finally {
                if (lock.isLocked()) {
                    lock.unlock();
                }
            }


            if (StudyType.DICTATION.equals(form.getStudyType())) {
                //智能听写：听写近期拼写出错频率较高的单词！
                wordsDtos = studyWordRecordsService.dictation(form, userId);
            } else {
                wordsDtos = wordsService.getStudyWords(form, studyBookRate);
            }
        }

        //添加释义
        wordsService.addSymbol(wordsDtos);

        Map<String, Object> map = new HashMap<>(4);
        map.put("total", wordsDtos == null ? 0 : wordsDtos.size());
        map.put("words", wordsDtos);
        return AppResponse.ok(map);
    }

    /**
     * 听读训练校对
     *
     * @return
     */
    @PostMapping("/listenCheck")
    @Transactional
    public Object listenCheck(@Validated ListenCheckForm form) {
        return new AppErrorResponse("听读训练暂不开放！");
    }

    /**
     * 对比测试分数
     *
     * @return
     */
    @GetMapping("/unitScore")
    @Transactional
    public Object unitScore(@RequestParam Integer bookId, @RequestParam Integer unitId) {
        //对比测试分数
        Map map = studyTestRecordsService.unitScore(bookId, unitId, getUserId());
        return AppResponse.ok(map);
    }

    /**
     * 综合学习完成
     *
     * @return
     */
    @PostMapping("/studySave")
    @Transactional
    public Object studySave(@RequestBody @Validated StudySaveForm form) {
        if (null == form.getStudyType()) {
            form.setStudyType(StudyType.REVIEW);
        }
        Integer userId = getUserId();
        //查看课程单元记录
        StudyBookRate studyBookRate = studyBookRateService.getStudyBookRateByBookIdAndUserIdAndUnitId(form.getBookId(), userId, form.getUnitId());
        if (StudyType.LEARN.equals(form.getStudyType()) && studyBookRate == null) {
            log.error("请求错误：错误码2001(找不到对应得课程单元学习记录)");
            throw new AppBadRequestException("学习保存失败！");
        }

        if (StudyType.LISTEN_TEST.equals(form.getStudyType())) {
            //听读训练，判断是否可以进行听读训练
            studyBookRateService.check(studyBookRate);
        }

        //学习单词记录保存
        studyWordRecordsService.studySave(form, userId, studyBookRate);

        //根据学习类型更新课程单元状态
        if (StudyType.LEARN.equals(form.getStudyType())) {
            //查询是否学完本单元
            Integer maxWordId = wordsService.getMaxWordIdByUnit(form.getBookId(), form.getUnitId());

            //学习完成状态
            boolean b;
            Integer lastWordsId = studyBookRate.getLastWordsId();
            if (lastWordsId == null || maxWordId == null) {
                b = false;
            } else {
                b = lastWordsId.equals(maxWordId);
            }
            studyBookRateService.updateStudyState(form.getStudyType(), studyBookRate, b);
        }

        //学习状态更新
        appUserService.studySave(form, userId);
        return SUCCESS;
    }

    /**
     * 综合测试
     *
     * @return
     */
    @GetMapping("/test")
    @Transactional
    public Object test(@Validated WordsTestForm form) {
        Map<String, List<TestWordsDto>> testWords;
        Integer userId = getUserId();
        if (form.getAutoTestType() != null) {
            //1汉译英，2英译汉，3听选，4听写
//            if ((form.getWordType() == null || form.getSize() == null)) {
            if (form.getSize() == null) {
                log.error("请求错误：错误码2003(size参数为空！)");
                throw new AppBadRequestException("请求错误");
            }

            testWords = wordsService.getTestWords(form, userId);
        } else {
            //5单元测试，6一测到底，7学前测试，8学后测试

            TestType testType = form.getTestType();
            if (form.getTestType() == null || form.getBookId() == null) {
                log.error("请求错误：错误码2000(bookId或unitId为空)");
                throw new AppBadRequestException("请求错误");
            }
            if (!TestType.UNTIL_TEST.equals(testType) && form.getUnitId() == null) {
                log.error("请求错误：错误码2000(bookId或unitId为空)");
                throw new AppBadRequestException("请求错误");
            }

            //判断是否可以进行5单元测试，6一测到底，8学后测试
            studyBookRateService.checkTest(testType, form.getBookId(), form.getUnitId(), userId);

            //获取数据源
            testWords = wordsService.getAllTestWords(ApiPageFactory.getPage(), form, userId);
        }

        Map<String, Object> map = new HashMap<>(2);
        map.put("testPaper", testWords);

        if (form.getTestType() != null && TestType.UNTIL_TEST.equals(form.getTestType())) {
            //获取总数，一测到底
            Long total = wordsService.getTotalByBookId(form.getBookId());
            map.put("total", total);
        }
        return AppResponse.ok(map);
    }


    /**
     * 测试完成
     *
     * @return
     */
    @PostMapping("/testSave")
    @Transactional
    public Object testSave(@RequestBody @Validated WordsTestSaveForm form) {
        final RLock lock = redisson.getLock(CacheName.COURSE_LOCK + getUserId());
        try {
            final boolean isLocked = lock.tryLock(5, 15, TimeUnit.SECONDS);
            if (!isLocked) {
                return AppResponse.badRequest("服务器繁忙，请稍后再试");
            }
            doTestSave(form);
        } catch (InterruptedException e) {
            log.error("Error while getting appuser redis lock.", e);
            return AppResponse.badRequest("服务器繁忙，请稍后再试");
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }

        return SUCCESS;
    }

    private void doTestSave(WordsTestSaveForm form) {
        Integer userId = getUserId();
        if (form.getAutoTestType() != null) {
            //1汉译英，2英译汉，3听选，4听写,修改测试次数
            studyWordRecordsService.updateTestTime(form.getWords(), userId);
        } else {
            //5单元测试，6一测到底，7学前测试，8学后测试
            TestType testType = form.getTestType();
            if (form.getTestType() == null || form.getBookId() == null || (!TestType.UNTIL_TEST.equals(testType) && form.getUnitId() == null)) {
                log.error("请求错误：错误码2000(bookId或unitId为空)");
                throw new AppBadRequestException("请求错误");
            }

            //判断是否可以进行5单元测试，6一测到底，8学后测试
            studyBookRateService.checkTest(testType, form.getBookId(), form.getUnitId(), userId);
            if (!TestType.UNTIL_TEST.equals(testType)) {
                //5单元测试，7学前测试，8学后测试,添加学习记录
                studyBookRateService.addStudyBookRateByTest(form, userId);
            }
        }
        //保存测试记录
        studyTestRecordsService.saveTestRecords(form, userId);

        //更新测试时长
        appUserService.updateTestTime(userId, form.getTimes());
    }
}
