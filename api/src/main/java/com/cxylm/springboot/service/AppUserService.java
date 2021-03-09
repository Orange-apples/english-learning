package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.form.StudySaveForm;
import com.cxylm.springboot.dto.form.UserRegisterForm;
import com.cxylm.springboot.dto.result.AppuserDto;
import com.cxylm.springboot.enums.AccountType;
import com.cxylm.springboot.model.AppUser;
import org.jetbrains.annotations.Nullable;


public interface AppUserService extends IService<AppUser> {
    /**
     * 通过手机查找用户
     */
    @Nullable
    AppUser findByMobile(String mobile, boolean isMerchant);
    @Nullable AppUser findByUsername(String username, boolean isMerchant);

    /**
     * 用户登录
     *
     * @return 用户id
     */
    AppUser userSMSLogin(String mobile, String smsCode, AccountType accountType);

    AppUser userLogin(String username, String password, boolean isMerchant);

    /**
     * 用户注册
     */
    AppUser register(UserRegisterForm form, AccountType accountType);

    /**
     * 学习状态更新
     * @param form
     * @param userId
     */
    void studySave(StudySaveForm form, Integer userId);

    /**
     * 更新测试时长
     * @param userId
     * @param times
     */
    void updateTestTime(Integer userId, Integer times);

    /**
     * 查询当日新增学员
     * @param accountState
     * @return
     */
    Integer todayIncrese(Integer accountState, Integer merchant, Integer schoolUserId);

    /**
     * 根据会员名查询学员访问信息
     * @param nickName
     * @param accountState
     * @return
     */
    Page<AppuserDto> selectStudentVisitInfo(Page<AppuserDto> page, Integer schoolUserId, String nickName,Integer accountState, Integer merchant);
}