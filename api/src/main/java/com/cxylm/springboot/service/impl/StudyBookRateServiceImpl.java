package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.StudyBookRateMapper;
import com.cxylm.springboot.dto.WXPushMessage;
import com.cxylm.springboot.dto.bookrate.BookStudyReport;
import com.cxylm.springboot.dto.form.StudyForm;
import com.cxylm.springboot.dto.form.WordsTestSaveForm;
import com.cxylm.springboot.dto.result.BooksUnitStateDto;
import com.cxylm.springboot.dto.result.MyBooksDto;
import com.cxylm.springboot.enums.StudyRateState;
import com.cxylm.springboot.enums.StudyType;
import com.cxylm.springboot.enums.TestType;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.BookInfo;
import com.cxylm.springboot.model.StudyBookRate;
import com.cxylm.springboot.model.Words;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.AppUserService;
import com.cxylm.springboot.service.BooksService;
import com.cxylm.springboot.service.StudyBookRateService;
import com.cxylm.springboot.service.WordsService;
import com.cxylm.springboot.service.mq.MQService;
import com.cxylm.springboot.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StudyBookRateServiceImpl extends ServiceImpl<StudyBookRateMapper, StudyBookRate> implements StudyBookRateService {
    private final MQService mqService;
    private final WordsService wordsService;
    private final BooksService booksService;
    private final AppUserService appUserService;

    @Override
    public Page<BookStudyReport> bookStudyReport(Page<BookStudyReport> page, Integer userId) {
        List<BookStudyReport> bookStudyReportList = baseMapper.bookStudyReport(page, userId);
        bookStudyReportList.forEach(e -> {
            LambdaQueryWrapper<Words> queryWrapper = new LambdaQueryWrapper<Words>()
                    .eq(Words::getBookId, e.getBookId())
                    .eq(Words::getUnit, e.getUnitId());
            e.setWordsCount(wordsService.count(queryWrapper));
        });
        page.setRecords(bookStudyReportList);
        return page;
    }

    @Override
    public int getTryCourseCount(Integer userId) {
        return baseMapper.getTryCourseCount(userId);
    }

    @Override
    public ResponseEntity buyUnit(Integer bookId, Integer userId, Integer unitId) {
        AppUser user = appUserService.getById(userId);
        Integer coin = user.getCoin();
        AppResponse appResponse = new AppResponse();
        appResponse.setCode(0);
        //避免重复购买单元
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<StudyBookRate>()
                .eq(StudyBookRate::getUserId, userId)
                .eq(StudyBookRate::getBookId, bookId)
                .eq(StudyBookRate::getUnitId, unitId));
        if (count > 0) {
            appResponse.setCode(-1);
            appResponse.setMessage("该单元已开通,请刷新页面重试");
            return new ResponseEntity<Object>(appResponse, HttpStatus.OK);
        }

        if (coin < 50) {
            appResponse.setCode(-1);
            appResponse.setMessage("您的金币余额不足");
            return new ResponseEntity<Object>(appResponse, HttpStatus.OK);
        }

        coin -= 50;
        user.setCoin(coin);
        boolean userSave = appUserService.updateById(user);

        if (userSave) {
            //开通对应单元学习信息
            StudyBookRate bookRate = new StudyBookRate();
            bookRate.setUserId(userId);
            bookRate.setBookId(bookId);
            bookRate.setUnitId(unitId);
            bookRate.setCreateTime(System.currentTimeMillis());
            bookRate.setState(StudyRateState.LEARNING);
            int rateSave = baseMapper.insert(bookRate);
            if (rateSave > 0) {
                appResponse.setMessage("开通成功");
                return new ResponseEntity<Object>(appResponse, HttpStatus.OK);
            }
        }

        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        appResponse.setCode(-1);
        appResponse.setMessage("开通失败");
        return new ResponseEntity<Object>(appResponse, HttpStatus.OK);
    }

    /**
     * 校验课程开通情况
     *
     * @param bookId
     * @param userId
     * @return
     */
    @Override
    public boolean checkOpenState(Integer bookId, Integer userId, Integer type) {
        //TODO 需校验bookId是否存在，暂时不写
//        Integer count = baseMapper.selectCount(new QueryWrapper<StudyBookRate>().eq("book_id", bookId).eq("user_id", userId).eq("is_try",0));
        Integer count = baseMapper.selectCount(new QueryWrapper<StudyBookRate>()
                .eq("book_id", bookId)
                .eq("user_id", userId)
                .eq("unit_id", 0)
                .eq("state", 0)
                .eq("is_try", type));
        if (count == null || count == 0) {
            //未开通
            return false;
        }
        //已开通
        return true;
    }

    /**
     * 开通课程
     *
     * @param bookId
     * @param userId
     */
    @Override
    public void OpenCourse(Integer bookId, Integer userId, Long expireTime) {
        //如果开通过使用的,删除试用信息
        UpdateWrapper<StudyBookRate> updateWrapper = new UpdateWrapper<StudyBookRate>()
                .eq("user_id", userId)
                .eq("book_id", bookId);
        baseMapper.delete(updateWrapper);

        StudyBookRate studyBookRate = new StudyBookRate();
        studyBookRate.setBookId(bookId);
        studyBookRate.setUserId(userId);
        studyBookRate.setCreateTime(System.currentTimeMillis());
        // 默认过期时间一年
        if (expireTime == null) expireTime = System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000;

        studyBookRate.setExpireTime(expireTime);
        // 课程过期后一年内还能再复习, 因此复习过期时间为课程过期时间再加一年
        studyBookRate.setReviewExpireTime(expireTime + 365L * 24 * 60 * 60 * 1000);
        boolean b = studyBookRate.insert();
        if (!b) {
            throw new AppBizException("开通失败！");
        }
    }

    @Override
    public void saveFreeCourse(Integer bookId, Integer userId, @Nullable Long expireTime) {
        StudyBookRate studyBookRate = new StudyBookRate();
        studyBookRate.setBookId(bookId);
        studyBookRate.setUserId(userId);
        studyBookRate.setIsTry(1);
        studyBookRate.setCreateTime(System.currentTimeMillis());
        // 默认过期时间一年
        if (expireTime == null) expireTime = System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000;

        studyBookRate.setExpireTime(expireTime);
        // 课程过期后一年内还能再复习, 因此复习过期时间为课程过期时间再加一年
        studyBookRate.setReviewExpireTime(expireTime + 365L * 24 * 60 * 60 * 1000);
        boolean b = studyBookRate.insert();
        if (!b) {
            throw new AppBizException("开通失败！");
        }
    }

    /**
     * 获取个人课程
     *
     * @param userId
     * @return
     */
    @Override
    public List<MyBooksDto> getMyBooks(Integer userId) {
        return baseMapper.getMyBooks(userId);
    }

    @Override
    public List<MyBooksDto> getBooksByStatus(Integer userId, Integer status) {
        return baseMapper.getBooksByStatus(userId, status);
    }

    /**
     * 该课程各单元学习状况
     *
     * @param bookId
     * @param userId
     * @return
     */
    @Override
    public List<BooksUnitStateDto> getBookUnitState(Integer bookId, Integer userId) {
        List<BooksUnitStateDto> unitState = baseMapper.getBookUnitState(bookId, userId);
        //查看课程是否为试用
        Integer isTry = this.getBookIsTry(bookId, userId);
        if (isTry.equals(1)) {
            for (int i = 0; i < unitState.size(); i++) {
                BooksUnitStateDto unitStateDto = unitState.get(i);
                //试用课程前两个单元为试用状态,后面的锁死
                if (i >= 2) {
                    unitStateDto.setState(StudyRateState.CREATE);
                } else {
                    unitStateDto.setState(StudyRateState.TRY);
                }
            }
        }
        return unitState;
    }

    /**
     * 添加学习记录
     *
     * @param form
     * @param userId
     */
    @Override
    public StudyBookRate addStudyBookRate(StudyForm form, Integer userId) {
        StudyBookRate studyBookRate = getStudyBookRateByBookIdAndUserIdAndUnitId(form.getBookId(), userId, form.getUnitId());
        boolean b = true;
        if (studyBookRate == null) {
            //新增学习记录
            studyBookRate = new StudyBookRate();
            BeanUtils.copyProperties(form, studyBookRate);
            studyBookRate.setUserId(userId);
            studyBookRate.setState(StudyRateState.LEARNING);
            studyBookRate.setCreateTime(System.currentTimeMillis());
            b = studyBookRate.insert();
        } else if (studyBookRate.getState().equals(StudyRateState.TEST_AND_CREATE)) {
            //状态为测试完成，则修改状态为学习中
            studyBookRate.setState(StudyRateState.TEST_AND_LEARNING);
            b = studyBookRate.updateById();
        }/*else if(studyBookRate.getState().equals(StudyRateState.WORDS_OVER) || studyBookRate.getState().equals(StudyRateState.TEST_AND_WORDS_OVER)){
            log.error("该单元已学习，请勿重复学习！");
            throw new AppBadRequestException("该单元已学习，请勿重复学习！");
        }*/
        if (!b) {
            throw new AppBizException("学习系统异常！");
        }
        return studyBookRate;
    }

    /**
     * 根据bookId、userId、unitId获取StudyBookRate
     *
     * @param bookId
     * @param userId
     * @param unitId
     * @return
     */
    @Override
    public StudyBookRate getStudyBookRateByBookIdAndUserIdAndUnitId(Integer bookId, Integer userId, Integer unitId) {
        return baseMapper.getStudyBookRateByBookIdAndUserIdAndUnitId(bookId, userId, unitId);
//        return baseMapper.selectOne(new QueryWrapper<StudyBookRate>().eq("book_id", bookId).eq("user_id", userId).eq("unit_id", userId));
    }

    /**
     * 根据学习类型更新课程单元状态
     *
     * @param studyType
     * @param studyBookRate
     * @param overState     学习完成状态
     */
    @Override
    public void updateStudyState(StudyType studyType, StudyBookRate studyBookRate, boolean overState) {
        StudyRateState wordsOver = null;
        switch (studyType) {
            case LEARN:
                if (!overState) {
                    break;
                }
                StudyRateState state = studyBookRate.getState();
                boolean b = studyBookRate.getState() == StudyRateState.LEARNING || studyBookRate.getState() == StudyRateState.TEST_AND_LEARNING;

                if (!b) {
                    break;
                }
                //更新学习状态为完成

                if (StudyRateState.LEARNING.equals(state)) {
                    wordsOver = StudyRateState.WORDS_OVER;
                }
                if (StudyRateState.TEST_AND_LEARNING.equals(state)) {
                    wordsOver = StudyRateState.TEST_AND_WORDS_OVER;
                }

                if (StudyRateState.LEARNING.equals(state) || StudyRateState.TEST_AND_LEARNING.equals(state)) {
                    //if (studyBookRate.getUnitId() >= wordsService.getMaxUnit(studyBookRate.getBookId())) {
                    AppUser appUser = appUserService.getById(studyBookRate.getUserId());
                    BookInfo bookInfo = booksService.getById(studyBookRate.getBookId());
                    if (appUser == null || bookInfo == null) break;
                    String wxOpenId = appUser.getWxOpenId();
                    if (wxOpenId != null) {
                        WXPushMessage message = new WXPushMessage();
                        message.openId = wxOpenId;
                        message.text = String.format("尊敬的家长您好，您的孩子已完成课程 %s 第%s单元的学习，请及时检查学习成果",
                                bookInfo.getName(), studyBookRate.getUnitId());
                        mqService.sendMsgToMQ("cxylm.wx.push", message);
                    }
                    //}
                }
                break;
            case LISTEN_TEST:
                switch (studyBookRate.getState()) {
                    //更新听读状态为完成
                    case WORDS_OVER:
                        wordsOver = StudyRateState.LISTEN_OVER;
                        break;
                    case TEST_AND_WORDS_OVER:
                        wordsOver = StudyRateState.TEST_AND_LISTEN_OVER;
                        break;
                    case UNIT_OVER:
                    case TEST_AND_UNIT_OVER:
                        wordsOver = StudyRateState.LISTEN_AND_UNIT_OVER;
                        break;
                }
                break;
        }
        studyBookRate.setState(wordsOver);
        boolean b = studyBookRate.updateById();
        if (!b) {
            log.error("系统异常！错误码:1003(更新学习状态异常)");
            throw new AppBizException("系统异常！错误码:1003");
        }
    }

    /**
     * 判断是否可以进行听读训练、单元测试和学后测试
     *
     * @param bookId
     * @param unitId
     * @param userId
     */
    @Override
    public void check(Integer bookId, Integer unitId, Integer userId) {
        StudyBookRate studyBookRate = getStudyBookRateByBookIdAndUserIdAndUnitId(bookId, userId, unitId);
        check(studyBookRate);
    }

    /**
     * 判断是否可以进行听读训练、单元测试和学后测试
     *
     * @param studyBookRate
     */
    @Override
    public void check(StudyBookRate studyBookRate) {
        if (studyBookRate == null || studyBookRate.getState().equals(StudyRateState.CREATE)
                || studyBookRate.getState().equals(StudyRateState.LEARNING)
                || studyBookRate.getState().equals(StudyRateState.TEST_AND_CREATE)
                || studyBookRate.getState().equals(StudyRateState.TEST_AND_LEARNING)) {
            log.error("请求错误：错误码2002（单词学习没有学习，不能进行其他训练和测试）");
            throw new AppBadRequestException("请先进行单词学习！");
        }
    }

    /**
     * 判断是否可以进行5单元测试，6一测到底，8学后测试
     *
     * @param testType
     * @param bookId
     * @param unitId
     * @param userId
     */
    @Override
    public void checkTest(TestType testType, Integer bookId, Integer unitId, Integer userId) {
        if (TestType.UNIT_TEST.equals(testType) || TestType.AFTER_TEST.equals(testType)) {
            //5单元测试，8学后测试
            check(bookId, unitId, userId);
        } else if (TestType.UNTIL_TEST.equals(testType)) {
            //6一测到底
            boolean b = baseMapper.checkUntilTest(bookId, userId);
            if (!b) {
                log.error("请求错误：错误码2004(该课程未完全学习完不能进行一测到底！)");
                throw new AppBadRequestException("该课程未完全学习完不能进行一测到底！");
            }
        }
    }

    /**
     * 5单元测试，7学前测试，8学后测试
     * 添加学习记录
     *
     * @param form
     * @param userId
     */
    @Override
    public void addStudyBookRateByTest(WordsTestSaveForm form, Integer userId) {
        StudyBookRate studyBookRate = getStudyBookRateByBookIdAndUserIdAndUnitId(form.getBookId(), userId, form.getUnitId());
        boolean b = true;

        StudyRateState studyRateState = null;
        switch (form.getTestType()) {
            case UNIT_TEST:
                //5单元测试
                if (StudyRateState.WORDS_OVER.equals(studyBookRate.getState())) {
                    //如果是单词学习完成（2），则修改为单元测试完成（4）
                    studyRateState = StudyRateState.UNIT_OVER;
                } else if (StudyRateState.LISTEN_OVER.equals(studyBookRate.getState())) {
                    //如果是单词学习完成（3），则修改为听读训练和单元训练完成（5）
                    studyRateState = StudyRateState.LISTEN_AND_UNIT_OVER;
                } else if (StudyRateState.TEST_AND_WORDS_OVER.equals(studyBookRate.getState())) {
                    //如果是单词学习完成（12），则修改为单元测试完成（14）
                    studyRateState = StudyRateState.UNIT_OVER;
                } else if (StudyRateState.TEST_AND_LISTEN_OVER.equals(studyBookRate.getState())) {
                    //如果是单词学习完成（13），则修改为听读训练和单元训练完成（15）
                    studyRateState = StudyRateState.TEST_LISTEN_AND_UNIT_OVER;
                }
                break;
            case BEFORE_TEST:
                //7学前测试
                if (studyBookRate.getState() == null) {
                    //无记录则新增学习记录
                    studyBookRate = new StudyBookRate();
                    BeanUtils.copyProperties(form, studyBookRate);
                    studyBookRate.setUserId(userId);
                    studyBookRate.setState(StudyRateState.TEST_AND_CREATE);
                    studyBookRate.setCreateTime(System.currentTimeMillis());
                    b = studyBookRate.insert();
                    if (!b) {
                        throw new AppBizException("学习系统异常！");
                    }
                } else {
                    studyRateState = getAfterStudyRateState(studyBookRate.getState());
                }
                break;
            case AFTER_TEST:
                //8学后测试
                studyRateState = getAfterStudyRateState(studyBookRate.getState());
                break;
        }

        if (studyRateState != null) {
            studyBookRate.setState(studyRateState);
            b = studyBookRate.updateById();
            if (!b) {
                throw new AppBizException("学习系统异常！");
            }
        }
    }

    @Override
    public List<StudyBookRate> scanExpiredCourse() {
        return baseMapper.scanExpiredCourse(System.currentTimeMillis());
    }

    @Override
    public void openFreeCourse(Integer bookId, Integer userId) {
        BookInfo bookInfo = booksService.getById(bookId);
        AssertUtil.isTrue(bookInfo != null, "课程不存在，请联系平台");

        boolean opened = this.checkOpenState(bookId, userId, 1);
        AssertUtil.isFalse(opened, "该课程已经开通了");
        AssertUtil.isTrue(baseMapper.getTryCourseCount(userId) < 2, "最多开通2个免费课程");

        // 免费课程默认一年有效期
        this.saveFreeCourse(bookId, userId, null);
    }

    @Override
    public Integer getBookIsTry(Integer bookId, Integer userId) {
        QueryWrapper<StudyBookRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("is_try");
        queryWrapper.eq("book_id", bookId);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("unit_id", 0);
        queryWrapper.eq("state", 0);

        List<StudyBookRate> list = baseMapper.selectList(queryWrapper);
        return list.get(0).getIsTry();
    }

    @Override
    public boolean isCourseExpired(Integer bookId, Integer userId) {
        return baseMapper.isCourseExpired(bookId, userId, System.currentTimeMillis());
    }

    private StudyRateState getAfterStudyRateState(StudyRateState state) {
        StudyRateState studyRateState = null;
        switch (state) {
            case CREATE:
                //如果是（0），则修改为（10）
                studyRateState = StudyRateState.TEST_AND_CREATE;
                break;
            case LEARNING:
                //如果是（1），则修改为（11）
                studyRateState = StudyRateState.TEST_AND_LEARNING;
                break;
            case WORDS_OVER:
                //如果是（2），则修改为（12）
                studyRateState = StudyRateState.TEST_AND_WORDS_OVER;
                break;
            case LISTEN_OVER:
                //如果是（3），则修改为（13）
                studyRateState = StudyRateState.TEST_AND_LISTEN_OVER;
                break;
            case UNIT_OVER:
                //如果是（4），则修改为（14）
                studyRateState = StudyRateState.TEST_AND_UNIT_OVER;
                break;
            case LISTEN_AND_UNIT_OVER:
                //如果是（5），则修改为（15）
                studyRateState = StudyRateState.TEST_LISTEN_AND_UNIT_OVER;
                break;
        }
        return studyRateState;
    }
}
