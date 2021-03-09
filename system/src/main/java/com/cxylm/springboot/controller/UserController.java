package com.cxylm.springboot.controller;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.UserManageDto;
import com.cxylm.springboot.dto.WXPushMessage;
import com.cxylm.springboot.dto.form.*;
import com.cxylm.springboot.dto.result.MyBooksDto;
import com.cxylm.springboot.enums.AccountType;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.BookInfo;
import com.cxylm.springboot.model.StudyBookRate;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.AppUserService;
import com.cxylm.springboot.service.BookInfoService;
import com.cxylm.springboot.service.MQService;
import com.cxylm.springboot.service.StudyBookRateService;
import com.cxylm.springboot.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cxylm.springboot.constant.AppMessage.ERROR_RECORD_NOT_EXIST;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserController extends ApiController {

    private final AppUserService appUserService;
    private final StudyBookRateService studyBookRateService;
    private final BookInfoService bookInfoService;
    private final MQService mqService;

    /**
     * 获取用户管理列表
     */
    @GetMapping("/user/list")
    public ResponseEntity<?> userList(AppUserSearchForm form) {
        Page<UserManageDto> userList = appUserService.getUserListByManage(ApiPageFactory.getPage(), form);
        return AppResponse.ok(userList);
    }

    @GetMapping("/app-user/{id}")
    public ResponseEntity<?> getAppUser(@PathVariable Integer id) {
        final AppUser appUser = appUserService.getById(id);
        return AppResponse.ok(appUser);
    }

    @PutMapping("/app-user/{id}")
    public ResponseEntity<?> updateAppUser(@PathVariable Integer id, @RequestBody AppUser form) {
        final AppUser appUser = appUserService.getById(id);
        AssertUtil.badRequestWhenNull(appUser, ERROR_RECORD_NOT_EXIST);
        BeanUtils.copyProperties(form, appUser, "id", "createTime", "updateTime");
        appUserService.updateById(appUser);
        return AppResponse.ok(appUser);
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserRegisterForm form) {
        final AppUser appUser = appUserService.register(form, AccountType.STUDENT);
        return AppResponse.ok(appUser);
    }

    @GetMapping("/user/{userId}/opened-course")
    public ResponseEntity<?> getUserOpenedCourse(@PathVariable Integer userId) {
        List<MyBooksDto> myBooks = studyBookRateService.getMyBooks(userId);
        return AppResponse.ok(myBooks);
    }

    @PostMapping("/user/open-course")
    @Transactional
    public ResponseEntity<?> openCourse(@RequestBody @Validated UserCourseBindForm form) {
        boolean isOpened = studyBookRateService.checkOpenState(form.getCourseId(), form.getUserId());
        if (isOpened) return AppResponse.badRequest("已经开通此课程");
        BookInfo bookInfo = bookInfoService.getById(form.getCourseId());
        AssertUtil.bizErrorWhenNull(bookInfo, "课程不存在");

        final AppUser appUser = appUserService.getById(form.getUserId());
        AssertUtil.badRequestWhenNull(appUser, "用户不存在");
        StudyBookRate studyBookRate = new StudyBookRate();
        studyBookRate.setBookId(form.getCourseId());
        studyBookRate.setUserId(form.getUserId());
        studyBookRate.setCreateTime(System.currentTimeMillis());
        studyBookRate.setExpireTime(form.getExpireTime());
        studyBookRate.insert();

        String wxOpenId = appUser.getWxOpenId();
        if (wxOpenId != null) {
            WXPushMessage message = new WXPushMessage();
            message.openId = wxOpenId;
            message.text = String.format("尊敬的家长您好，您的账号已开通课程【%s】，请积极监督孩子学习", bookInfo.getName()) ;
            mqService.sendMsgToMQ("cxylm.wx.push", message);
        }
        return SUCCESS;
    }

    @PostMapping("/user/open-course-batch")
    public ResponseEntity<?> openCourse(@RequestBody @Validated OpenCourseBatchForm form) {
        final AppUser appUser = appUserService.getById(form.getUserId());
        AssertUtil.badRequestWhenNull(appUser, "用户不存在");

        final List<BookInfo> bookInfoList =
                bookInfoService.list(new QueryWrapper<BookInfo>()
                        .eq("level", form.getLevel())
                        .eq("edition", form.getPublisher()));


        String wxOpenId = appUser.getWxOpenId();
        int count = 0;
        for (BookInfo bookInfo : bookInfoList) {
            boolean isOpened = studyBookRateService.checkOpenState(bookInfo.getId(), form.getUserId());
            if (isOpened) continue;
            count++;

            StudyBookRate studyBookRate = new StudyBookRate();
            studyBookRate.setBookId(bookInfo.getId());
            studyBookRate.setUserId(form.getUserId());
            studyBookRate.setCreateTime(System.currentTimeMillis());
            studyBookRate.setExpireTime(form.getExpireTime());
            studyBookRate.insert();

            if (wxOpenId != null) {
                WXPushMessage message = new WXPushMessage();
                message.openId = wxOpenId;
                message.text = String.format("尊敬的家长您好，您已开通课程【%s】，请积极监督孩子学习", bookInfo.getName()) ;
                mqService.sendMsgToMQ("cxylm.wx.push", message);
            }
        }
        Map<String, Object> res = new HashMap<>();
        res.put("count", count);
        return AppResponse.ok(res);
    }

    @PostMapping("/user/bind-school")
    public ResponseEntity<?> bindSchool(@RequestBody @Validated UserSchoolBindForm form) {
        final AppUser appUser = appUserService.getById(form.getUserId());
        AssertUtil.badRequestWhenNull(appUser, "用户不存在");
        appUser.setSchoolId(form.getSchoolId());
        appUser.updateById();
        return SUCCESS;
    }

    /**
     * 封禁/解封
     *
     * @param form
     * @return
     */
    @PutMapping("/user/managerUpdateUser")
    public Object managerUpdateStore(@RequestBody @Validated UserStateForm form) {
        appUserService.blockOrRecover(form.getUserId(), form.getState());
        return SUCCESS;
    }
}
