package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.UserManageDto;
import com.cxylm.springboot.dto.form.AppUserSearchForm;
import com.cxylm.springboot.dto.form.SchoolRegisterForm;
import com.cxylm.springboot.dto.form.UserRegisterForm;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.AccountType;
import com.cxylm.springboot.model.AppUser;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

public interface AppUserService extends IService<AppUser> {
    /**
     * 通过手机查找用户
     */
    @Nullable
    AppUser findByMobile(String mobile, boolean isMerchant);
    @Nullable AppUser findByUsername(String username, boolean isMerchant);

    @Transactional
    AppUser register(UserRegisterForm form, AccountType accountType);

    @Transactional
    AppUser registerSchoolUser(SchoolRegisterForm form);
    /**
     * 后台管理
     * 用户列表
     */
    Page<UserManageDto> getUserListByManage(Page<UserManageDto> page, AppUserSearchForm form);

    /**
     * 封禁或恢复用户
     *
     * @param userId
     * @param state
     */
    void blockOrRecover(Integer userId, AccountState state);
}
