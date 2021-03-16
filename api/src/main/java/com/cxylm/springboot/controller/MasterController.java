package com.cxylm.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.config.JwtHelper;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.dto.form.LoginForm;
import com.cxylm.springboot.dto.form.PasswordResetForm;
import com.cxylm.springboot.dto.form.UserRegisterForm;
import com.cxylm.springboot.dto.result.AppuserDto;
import com.cxylm.springboot.dto.result.LoginRecordDto;
import com.cxylm.springboot.dto.wordRecord.StudentTestRecordDto;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.AccountType;
import com.cxylm.springboot.enums.SchoolEnum;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.LoginRecord;
import com.cxylm.springboot.model.StudyWordRecords;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.AppUserService;
import com.cxylm.springboot.service.LoginRecordService;
import com.cxylm.springboot.service.SMSService;
import com.cxylm.springboot.service.StudyWordRecordsService;
import com.cxylm.springboot.util.AssertUtil;
import com.cxylm.springboot.validator.group.PasswordLoginGroup;
import com.cxylm.springboot.validator.group.SMSLoginGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: shiyanru
 * @Date: 2020/3/8 10:49
 */
@Slf4j
@RestController
@RequestMapping("/api/master")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MasterController extends ApiController {

    private final JwtHelper jwtTokenUtil;
    private final RedissonClient redisson;
    private final AppUserService appUserService;
    private final LoginRecordService loginRecordService;
    private final SMSService smsService;
    private final StudyWordRecordsService wordRecordsService;

    @PostMapping(value = "/register")
    @PublicAPI
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserRegisterForm form) {
        final RLock lock = redisson.getLock(CacheName.APP_USER_LOCK_NAME + form.getUsername());

        AppUser appUser;
        try {
            final boolean isLocked = lock.tryLock(5, 60, TimeUnit.SECONDS);
            if (!isLocked) {
                return AppResponse.badRequest("服务器繁忙，请稍后再试");
            }
            appUser = appUserService.register(form, AccountType.MASTER);
        } catch (InterruptedException e) {
            log.error("Error while getting appuser redis lock.", e);
            return AppResponse.badRequest("服务器繁忙，请稍后再试");
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
        final String token = jwtTokenUtil.generateToken(appUser.getId());
        return AppResponse.ok(new JwtResponse(token, appUser.getId(), appUser));
    }

    /**
     * 普通登录
     */
    @PostMapping(value = "/login")
    @PublicAPI
    public ResponseEntity<?> userLogin(@RequestBody @Validated({PasswordLoginGroup.class, Default.class}) LoginForm loginForm) {
        AppUser appUser = appUserService.userLogin(loginForm.getUsername(), loginForm.getPassword(), true);
        if (appUser == null || appUser.getMerchant() != AccountType.MASTER) {
            log.info("该账号不是校长身份账号,检查用户merchant字段");
            return AppResponse.badRequest("用户名或密码错误");
        }

        if(!appUser.getSchoolState().equals(SchoolEnum.SCHOOL_ENABLE_1.getValue())){
            return AppResponse.badRequest("该校区账户已被禁用，请联系管理员");
        }
        final String token = jwtTokenUtil.generateToken(appUser.getId());
        log.info("用户{}登录成功, IP地址{}", loginForm.getUsername(), getIP());

        // TODO: 2020/3/17  校长端不保存登录记录
        //loginRecordService.addLoginRecord(appUser.getId());
        return AppResponse.ok(new JwtResponse(token, appUser.getId(), appUser));
    }

    @PostMapping(value = "/user/password/reset")
    public ResponseEntity<?> setInfo(@RequestBody @Validated PasswordResetForm form) {
        smsService.checkSMSCode(form.getMobile(), form.getSmsCode());
        AppUser appUser = appUserService.findByMobile(form.getMobile(), true);
        AssertUtil.badRequestWhenNull(appUser, "账号不存在");

        appUser.setPwd(form.getPassword());
        boolean b = appUser.updateById();
        if (!b) {
            log.error("账户{}密码重置错误！", appUser.getId());
            throw new AppBizException("账户设置错误！");
        }
        return SUCCESS;
    }

    @PostMapping(value = "/login/sms")
    @PublicAPI
    public ResponseEntity<?> userSMSLogin(@RequestBody @Validated({SMSLoginGroup.class, Default.class}) LoginForm loginForm) {
        AppUser appUser = appUserService.userSMSLogin(loginForm.getUsername(), loginForm.getSmsCode(), AccountType.MASTER);
        if (appUser == null) {
            return AppResponse.badRequest("用户名或密码错误");
        }
        final String token = jwtTokenUtil.generateToken(appUser.getId());
        log.info("用户{}登录成功, IP地址{}", loginForm.getUsername(), getIP());

        // TODO: 2020/3/17  校长端不保存登录记录 
        //loginRecordService.addLoginRecord(appUser.getId());
        return AppResponse.ok(new JwtResponse(token, appUser.getId(), appUser));
    }

    /**
     * 学员访问统计
     *
     * @param nickName
     * @return
     */
    @GetMapping(value = "/studentCount")
    public ResponseEntity<?> studentCount(String nickName) {
        Integer userId = getUserId();
        //学员总量
        Integer totalCount = appUserService.count(new QueryWrapper<AppUser>().eq("school_id", userId).ne("account_state", AccountState.LOGOUT.getValue())
                .eq("merchant", AccountType.STUDENT.getValue()));

        //当日新增学员数量
        Integer todayCount = appUserService.todayIncrese(AccountState.LOGOUT.getValue(), AccountType.STUDENT.getValue(), userId);

        //学员访问记录（昵称、性别、注册日期、访问次数）
        Page<AppuserDto> userList = appUserService.selectStudentVisitInfo(ApiPageFactory.getPage(), userId, nickName,
                AccountState.LOGOUT.getValue(), AccountType.STUDENT.getValue());

        Map map = new HashMap();
        map.put("totalCount", totalCount);
        map.put("todayCount", todayCount);
        map.put("userList", userList);
        return AppResponse.ok(map);
    }

    /**
     * 学生访问记录
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/studentVisitDetail")
    public ResponseEntity<?> studentVisitDetail(Integer userId) {
        if (userId == null) {
            return AppResponse.badRequest("请求错误");
        }

        //学员信息（昵称 注册日期 照片  ）
        AppUser user = appUserService.getById(userId);
        if (user == null) {
            return AppResponse.badRequest("该用户不存在");
        }
//        else if (user.getMerchant() != AccountType.MASTER){
//            return AppResponse.badRequest("当前用户暂无该权限");
//        }

        //学员访问记录（日期  访问次数）
        Page<LoginRecordDto> recordList = loginRecordService.selectRecordDetail(ApiPageFactory.getPage(), userId);

        Map map = new HashMap();
        map.put("user", user);
        map.put("recordList", recordList);
        return AppResponse.ok(map);
    }

    /**
     * 学生使用统计
     *
     * @return
     */
    @PublicAPI
    @GetMapping(value = "/studentUseCount")
    public ResponseEntity<?> studentUseCount() {
        Integer userId = getUserId();
        //累计使用人数
        Integer totalCount = loginRecordService.count(new QueryWrapper<LoginRecord>().eq("school_id", userId));

        //今日使用人数
        Integer todayCount = loginRecordService.selectTodayUse(userId);

        //使用记录
        Page<LoginRecordDto> recordList = loginRecordService.selectRecordList(ApiPageFactory.getPage());

        Map map = new HashMap();
        map.put("totalCount", totalCount);
        map.put("todayCount", todayCount);
        map.put("recordList", recordList);
        return AppResponse.ok(map);
    }

    /**
     * 学员访问记录
     *
     * @param id
     * @return
     */
    @GetMapping("studentTestRecord/{id}")
    public ResponseEntity<?> studentTestRecord(@PathVariable("id") Integer id) {
        Page<Object> page = ApiPageFactory.getPage();
        long size = page.getSize();
        long current = page.getCurrent();
        Page<StudentTestRecordDto> page1 = wordRecordsService.studentTestRecord(page, id);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("totalCount", page1.getTotal());
        map.put("recordList", page1.getRecords());
        return ResponseEntity.ok(map);
    }

    @Data
    @AllArgsConstructor
    private static class JwtResponse {
        private String token;
        private Integer userId;
        private AppUser appUser;
    }
}
