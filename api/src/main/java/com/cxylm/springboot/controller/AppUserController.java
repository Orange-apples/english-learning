package com.cxylm.springboot.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.config.JwtHelper;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.dto.form.*;
import com.cxylm.springboot.dto.response.WXOAuthResponse;
import com.cxylm.springboot.dto.result.UserInfoDto;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.AccountType;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.AppUserConfig;
import com.cxylm.springboot.model.BookInfo;
import com.cxylm.springboot.model.StudyBookRate;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.*;
import com.cxylm.springboot.util.AssertUtil;
import com.cxylm.springboot.util.WXUtil;
import com.cxylm.springboot.validator.group.PasswordLoginGroup;
import com.cxylm.springboot.validator.group.SMSLoginGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppUserController extends ApiController {
    private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;
    private final JWTUserDetailService userDetailsService;
    private final JwtHelper jwtTokenUtil;
    private final RedissonClient redisson;
    private final AppUserConfigService appUserConfigService;
    private final LoginRecordService loginRecordService;
    private final WXUtil wxUtil;
    private final SMSService smsService;
    private final StudyWordRecordsService studyWordRecordsService;
    private final BooksService bookInfoService;
    private final StudyBookRateService bookRateService;

    //@PostMapping(value = "/api/user/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginForm loginForm) throws Exception {
        authenticate(loginForm.getUsername(), loginForm.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginForm.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        //return ResponseEntity.ok(new JwtResponse(token, appUser.getId()));
        return SUCCESS;
    }


    @PostMapping(value = "/api/user/register")
    @PublicAPI
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserRegisterForm form) {
        final RLock lock = redisson.getLock(CacheName.APP_USER_LOCK_NAME + form.getUsername());

        AppUser appUser;
        try {
            final boolean isLocked = lock.tryLock(5, 60, TimeUnit.SECONDS);
            if (!isLocked) {
                return AppResponse.badRequest("服务器繁忙，请稍后再试");
            }
            appUser = appUserService.register(form, AccountType.STUDENT);

            // 开通免费课程
            final List<BookInfo> bookInfoList =
                    bookInfoService.list(new QueryWrapper<BookInfo>()
                            .eq("free", true));
            if (bookInfoList.size() > 0) {
                final List<StudyBookRate> collect = bookInfoList.stream().map(bookInfo -> {
                    StudyBookRate studyBookRate = new StudyBookRate();
                    studyBookRate.setBookId(bookInfo.getId());
                    studyBookRate.setUserId(appUser.getId());
                    studyBookRate.setCreateTime(System.currentTimeMillis());
                    studyBookRate.insert();
                    return studyBookRate;
                }).collect(Collectors.toList());
                bookRateService.saveBatch(collect);
            }
        } catch (InterruptedException e) {
            log.error("Error while getting appuser redis lock.", e);
            return AppResponse.badRequest("服务器繁忙，请稍后再试");
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
        final String token = jwtTokenUtil.generateToken(appUser.getId());
        return AppResponse.ok(new JwtResponse(token, appUser.getId()));
    }

    /**
     * 普通登录
     */
    @PostMapping(value = "/api/user/login")
    @PublicAPI
    public ResponseEntity<?> userLogin(@RequestBody @Validated({PasswordLoginGroup.class, Default.class}) LoginForm loginForm) {
        AppUser appUser = appUserService.userLogin(loginForm.getUsername(), loginForm.getPassword(), false);
        if (appUser == null) {
            return AppResponse.badRequest("用户名或密码错误");
        }

        //账号为禁用状态
        if (AccountState.LOCKED.equals(appUser.getAccountState())){
            return AppResponse.accountLocked();
        }

        Long expire = appUser.getExpire();
        if (expire != null && expire < System.currentTimeMillis()) {
            return AppResponse.badRequest("账号已失效，请联系平台重新激活");
        }
        final String token = jwtTokenUtil.generateToken(appUser.getId());
        log.info("用户{}登录成功, IP地址{}", loginForm.getUsername(), getIP());

        loginRecordService.addLoginRecord(appUser.getId(), appUser.getSchoolId());
        return AppResponse.ok(new JwtResponse(token, appUser.getId()));
    }

    @PostMapping(value = "/api/user/bind-wx")
    public ResponseEntity<?> userBInd(@RequestBody @Validated BindForm form) {
        AppUser appUser = appUserService.getById(getUserId());
        AssertUtil.isTrue(appUser != null);
        appUser.setWxOpenId(form.getOpenId());
        appUserService.updateById(appUser);
        return SUCCESS;
    }

    @PostMapping(value = "/api/user/login/sms")
    @PublicAPI
    public ResponseEntity<?> userSMSLogin(@RequestBody @Validated({SMSLoginGroup.class, Default.class}) LoginForm loginForm) {
        AppUser appUser = appUserService.userSMSLogin(loginForm.getMobile(), loginForm.getSmsCode(), AccountType.STUDENT);
        if (appUser == null) {
            return AppResponse.badRequest("用户名或密码错误");
        }
        final String token = jwtTokenUtil.generateToken(appUser.getId());
        log.info("用户{}登录成功, IP地址{}", loginForm.getUsername(), getIP());

        loginRecordService.addLoginRecord(appUser.getId(), appUser.getSchoolId());
        return AppResponse.ok(new JwtResponse(token, appUser.getId()));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Data
    @AllArgsConstructor
    private static class JwtResponse {
        private String token;
        private Integer userId;
    }

    /**
     * 系统设置
     *
     * @param appUserConfig
     * @return
     */
    @PostMapping(value = "/api/user/setConfig")
    public ResponseEntity<?> setConfig(@RequestBody @Validated AppUserConfig appUserConfig) {
        Integer userId = getUserId();
        appUserConfig.setUserId(userId);
        appUserConfigService.updateAppUserConfig(appUserConfig);
        return SUCCESS;
    }

    /**
     * 获取系统设置
     *
     * @return
     */
    @GetMapping(value = "/api/user/getConfig")
    public ResponseEntity<?> getConfig() {
        Integer userId = getUserId();
        AppUserConfig appUserConfig = appUserConfigService.getById(userId);
        Map<String, AppUserConfig> map = new HashMap<>(2);
        map.put("appUserConfig", appUserConfig);
        return AppResponse.ok(map);
    }

    /**
     * 设置头像
     * @return
     */
    @PostMapping(value = "/api/user/setAvatar")
    public ResponseEntity<?> setAvatar(@RequestBody JSONObject jsonObject) {
        String newAvatar = jsonObject.getString("avatar");
        if(StringUtils.isBlank(newAvatar)){
            throw new AppBizException("新头像链接不能为空！");
        }
        AppUser appUser = appUserService.getById(getUserId());
        appUser.setAvatar(newAvatar);
        boolean result = appUserService.updateById(appUser);
        if (result) {
            return SUCCESS;
        }
        throw new AppBizException("头像修改失败！");
    }

    /**
     * 账户设置
     *
     * @param userPwdForm
     * @return
     */
    @PostMapping(value = "/api/user/setPwd")
    public ResponseEntity<?> setPwd(@RequestBody @Validated UserPwdForm userPwdForm) {
        AppUser appUser = appUserService.getById(getUserId());
        BeanUtils.copyProperties(userPwdForm, appUser);
        String oldPwd = userPwdForm.getOldPwd();
        String newPwd = userPwdForm.getNewPwd();

        if (oldPwd != null && !"".equals(oldPwd.trim())) {
            //输入了密码
            if (oldPwd.equals(newPwd)) {
                throw new AppBadRequestException("新旧密码一致！");
            }
            if (!oldPwd.equals(appUser.getPwd())) {
                throw new AppBadRequestException("原密码错误！");
            }
            appUser.setPwd(newPwd);
        }
        boolean b = appUser.updateById();
        if (!b) {
            log.error("账户密码修改失败！");
            throw new AppBizException("账户密码修改失败！");
        }
        return SUCCESS;
    }


    /**
     * 账户设置
     *
     * @param userInfoForm
     * @return
     */
    @PostMapping(value = "/api/user/setInfo")
    public ResponseEntity<?> setInfo(@RequestBody @Validated UserInfoForm userInfoForm) {
        AppUser appUser = appUserService.getById(getUserId());

        BeanUtils.copyProperties(userInfoForm, appUser);
        String oldPwd = userInfoForm.getOldPwd();
        String newPwd = userInfoForm.getNewPwd();

        if (oldPwd != null && !"".equals(oldPwd.trim())) {
            //输入了密码
            if (oldPwd.equals(newPwd)) {
                throw new AppBadRequestException("新旧密码一致！");
            }
            if (!oldPwd.equals(appUser.getPwd())) {
                throw new AppBadRequestException("原密码错误！");
            }
            appUser.setPwd(newPwd);
        }
        boolean b = appUser.updateById();
        if (!b) {
            log.error("账户设置错误！");
            throw new AppBizException("账户设置错误！");
        }
        return SUCCESS;
    }

    /**
     * 获取账户设置
     *
     * @return
     */
    @GetMapping(value = "/api/user/getInfo")
    public ResponseEntity<?> getInfo() {
        AppUser appUser = appUserService.getById(getUserId());
        UserInfoDto userInfo = new UserInfoDto();
        BeanUtils.copyProperties(appUser, userInfo);
        Map<String, UserInfoDto> map = new HashMap<>(2);
        map.put("userInfo", userInfo);
        return AppResponse.ok(map);
    }

    @GetMapping(value = "/api/user/wx-access-token")
    @PublicAPI
    public ResponseEntity<?> getWXOpenId(@RequestParam String code) {
        WXOAuthResponse res = wxUtil.getJsAPIAccessTokenByCode(code);
        return res == null ? AppResponse.badRequest("获取用户信息失败") :
                AppResponse.ok(new WXResponse(res.getOpenid(), res.getAccess_token(), res.getRefresh_token()));
    }

    @PublicAPI
    @PostMapping(value = "/api/user/password/reset")
    public ResponseEntity<?> setInfo(@RequestBody @Validated PasswordResetForm form) {
        smsService.checkSMSCode(form.getMobile(), form.getSmsCode());
        AppUser appUser = appUserService.findByMobile(form.getMobile(), false);
        AssertUtil.badRequestWhenNull(appUser, "账号不存在");

        appUser.setPwd(form.getPassword());
        boolean b = appUser.updateById();
        if (!b) {
            log.error("账户{}密码重置错误！", appUser.getId());
            throw new AppBizException("账户设置错误！");
        }
        return SUCCESS;
    }

    @Data
    @AllArgsConstructor
    private static class WXResponse {
        private String openId;
        private String accessToken;
        private String refreshToken;
    }

    /**
     * 获取复习单词数
     *
     * @return
     */
    @GetMapping(value = "/api/user/getReviewWordsCounts")
    public ResponseEntity<?> getReviewWordsCounts() {
        Integer userId = getUserId();
        Integer b = studyWordRecordsService.checkReview(userId);
        Map map = new HashMap();
        if (b != null) {
//            AppUserConfig userConfig = appUserConfigService.getById(userId);
//            Integer maxWords = 20;
//            if (userConfig != null) {
//                maxWords = userConfig.getMaxReviewWord();
//            }
//            if (b < maxWords) {
//                maxWords = b;
//            }
//            map.put("counts", maxWords);
            map.put("counts", b);
        } else {
            map.put("counts", 0);
        }
        return AppResponse.ok(map);
    }

    /**
     * 是否强制复习
     *
     * @return
     */
    @GetMapping(value = "/api/user/hasReview")
    public ResponseEntity<?> hasReview() {
        Integer userId = getUserId();
        AppUser appUser = appUserService.getById(userId);
        AppUserConfig config = appUserConfigService.getById(userId);
        Map map = new HashMap(2);
        Integer b2 = studyWordRecordsService.checkReview(userId);
        boolean b = DateUtil.isSameDay(new Date(), new Date(appUser.getLastReviewTime()));
        map.put("hasReview", config.getAutoReview() && !b && b2 != null && b2 > 0);
        return AppResponse.ok(map);
    }
}
