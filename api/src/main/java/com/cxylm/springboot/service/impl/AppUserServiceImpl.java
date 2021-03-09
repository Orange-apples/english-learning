package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.AppUserMapper;
import com.cxylm.springboot.dao.StudyTestRecordsMapper;
import com.cxylm.springboot.dto.form.StudySaveForm;
import com.cxylm.springboot.dto.form.StudySaveWordsForm;
import com.cxylm.springboot.dto.form.UserRegisterForm;
import com.cxylm.springboot.dto.result.AppuserDto;
import com.cxylm.springboot.dto.result.TestResultDto;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.AccountType;
import com.cxylm.springboot.enums.StudyState;
import com.cxylm.springboot.enums.StudyType;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.Words;
import com.cxylm.springboot.service.AppUserService;
import com.cxylm.springboot.service.PwdService;
import com.cxylm.springboot.service.SMSService;
import com.cxylm.springboot.service.WordsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {
    private final SMSService smsService;
    private final PwdService pwdService;
    private final WordsService wordsService;
    private final AppUserRegService appUserRegService;
    private final StudyTestRecordsMapper studyTestRecordsMapper;

    /**
     * 重构getById方法，过滤注销状态的用户
     *
     * @param id
     * @return
     */
    @Override
    public AppUser getById(Serializable id) {
        final AppUser byId = baseMapper.selectById(id);
        if (byId == null) {
            throw new AppBadRequestException("用户数据异常");
        }
        if (byId.getAccountState() == AccountState.LOCKED) {
            throw new AppBadRequestException("账号已被封禁");
        }
        return byId;
    }

    @Override
    public @Nullable AppUser findByMobile(String mobile, boolean isMerchant) {
        return baseMapper.findByMobile(mobile, isMerchant);
    }

    @Override
    public @Nullable AppUser findByUsername(String username, boolean isMerchant) {
        return baseMapper.findByUsername(username, isMerchant);
    }

    @Override
    public AppUser userSMSLogin(String mobile, String smsCode, AccountType accountType) {
        final AppUser appUser = this.findByMobile(mobile, accountType == AccountType.MASTER);
        return smsCodeLogin(mobile, appUser, smsCode, accountType);
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    public AppUser userLogin(String username, String password, boolean isMerchant) {
        final AppUser appUser = this.findByUsername(username, isMerchant);
        if (appUser != null && password.equals(appUser.getPwd())) {
            return appUser;
        }
        return null;
    }

    @Override
    public AppUser register(UserRegisterForm form, AccountType accountType) {
        final String mobile = form.getMobile();
        AppUser appUser = this.findByMobile(mobile, accountType == AccountType.MASTER);
        if (appUser != null) {
            throw new AppBadRequestException("手机号已注册，请直接登录");
        }
        appUser = this.findByUsername(form.getUsername(), accountType == AccountType.MASTER);
        if (appUser != null) {
            throw new AppBadRequestException("该用户名已注册");
        }

        smsService.checkSMSCode(mobile, form.getSmsCode());

        appUser = appUserRegService.registerUser(mobile, form.getUsername(), form.getPassword(), accountType);
        return appUser;
    }

    /**
     * 短信验证码登录
     *
     * @return 用户id
     */
    private AppUser smsCodeLogin(@NotNull String mobile, @Nullable AppUser appUser, @Nullable String smsCode, @Nullable AccountType accountType) {
        smsService.checkSMSCode(mobile, smsCode);
        if (appUser == null) {
            // appUser = appUserRegService.registerUser(mobile, null, accountType);
            throw new AppBadRequestException("用户不存在, 请先前往注册");
        }
        return appUser;
    }

    /**
     * 密码登录
     *
     * @return 用户id
     */
    private AppUser passwordLogin(String inputPassword, @Nullable AppUser appUser) {
        if (appUser == null) {
//            appUser = this.registerMerchant(mobile, plainPassword);
            throw new AppBadRequestException("用户名或密码错误");
        } else {
            // check password
            pwdService.checkLoginPwd(appUser.getId(), inputPassword, appUser.getPwd());
        }
        return appUser;
    }

    /**
     * 学习状态更新
     *
     * @param form
     * @param userId
     */
    @Override
    public void studySave(StudySaveForm form, Integer userId) {
        Integer bookId = null;
        Integer unitId = null;
        Integer wordId = null;
        Integer unitState = null;
        Integer listenUnitId = null;
        Long lastReviewTime = null;
        List<StudySaveWordsForm> words = form.getWords();
        if (StudyType.LEARN.equals(form.getStudyType())) {
            //单词学习，更新学习状态
            bookId = form.getBookId();
            StudySaveWordsForm studySaveWordsForm = words.get(words.size() - 1);
            wordId = studySaveWordsForm.getWordId();
            unitId = form.getUnitId();
            unitState = StudyState.OVER.getValue();
        } else if (StudyType.LISTEN_TEST.equals(form.getStudyType())) {
            //听读训练，更新听读状态
            listenUnitId = form.getUnitId();
        }

        if(StudyType.REVIEW.equals(form.getStudyType())){
            lastReviewTime = System.currentTimeMillis();
        }

        Integer learningTime = form.getLearningTime();
        Integer spellTime = form.getSpellTime();
        Integer coin = words.size();
        boolean b = baseMapper.updateStudyState(userId, bookId, unitId, wordId, unitState, listenUnitId, coin, learningTime, spellTime, lastReviewTime);
        if (!b) {
            log.error("系统异常！错误码:1000");
            throw new AppBizException("系统异常！");
        }

    }

    /**
     * 更新测试时长
     *
     * @param userId
     * @param times
     */
    @Override
    public void updateTestTime(Integer userId, Integer times) {
        boolean b = baseMapper.updateTestTime(userId, times);
        if (!b) {
            log.error("系统异常！错误码:1004(更新测试时长失败)");
            throw new AppBizException("系统异常！");
        }
    }

    /**
     * 查询当日新增学员
     *
     * @param accountState
     * @return
     */
    @Override
    public Integer todayIncrese(Integer accountState, Integer merchant, Integer schoolUserId) {
        return baseMapper.todayIncrese(accountState, merchant, schoolUserId);
    }

    /**
     * 根据会员名查询学员访问信息
     *
     * @param nickName
     * @param accountState
     * @return
     */
    @Override
    public Page<AppuserDto> selectStudentVisitInfo(Page<AppuserDto> page, Integer schoolUserId, String nickName, Integer accountState, Integer merchant) {
        List<AppuserDto> users = baseMapper.selectStudentVisitInfo(page, schoolUserId, nickName, accountState, merchant);
        users.forEach(u -> {
            //获取用户的学习记录
            List<TestResultDto> testResult = studyTestRecordsMapper.getTestResult(null, u.getId());
            //设置最后一次学习的书本名称,单次数量
            if(testResult != null && testResult.size() > 0){
                TestResultDto result = testResult.get(0);
                int wordsCount = wordsService.count(new LambdaQueryWrapper<Words>().eq(Words::getBookId, result.getBookId()));
                u.setWordsCount(wordsCount);
                u.setScore(result.getScore());
                u.setBookName(result.getBookName());
            }else{
                u.setBookName("暂无学习记录");
                u.setWordsCount(0);
                u.setScore(0);
            }

        });
        page.setRecords(users);
        return page;
    }


}
