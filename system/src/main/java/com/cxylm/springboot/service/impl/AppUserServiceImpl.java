package com.cxylm.springboot.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.constant.Constant;
import com.cxylm.springboot.dao.AppUserMapper;
import com.cxylm.springboot.dto.UserManageDto;
import com.cxylm.springboot.dto.form.AppUserSearchForm;
import com.cxylm.springboot.dto.form.SchoolRegisterForm;
import com.cxylm.springboot.dto.form.UserRegisterForm;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.enums.AccountType;
import com.cxylm.springboot.enums.EnableState;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.AppUserConfig;
import com.cxylm.springboot.model.BD;
import com.cxylm.springboot.service.AppUserService;
import com.cxylm.springboot.service.SysBDService;
import com.cxylm.springboot.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {
    private Hashids nickNameHasher = new Hashids(Constant.NICKNAME_HASH_SALT, 4);
    private final SysBDService bdService;

    @Override
    //@Cacheable(cacheNames = CacheName.APP_USER, key = "#mobile")
    public @Nullable AppUser findByMobile(String mobile, boolean isMerchant) {
        return baseMapper.selectOne(new QueryWrapper<AppUser>().eq("mobile", mobile).eq("merchant", isMerchant));
    }

    @Override
    public @Nullable AppUser findByUsername(String username, boolean isMerchant) {
        return baseMapper.selectOne(new QueryWrapper<AppUser>().eq("username", username).eq("merchant", isMerchant));
    }

    @Override
    public AppUser register(UserRegisterForm form, AccountType accountType) {
        final Integer bdCode = form.getBdCode();

        if (bdCode != null) {
            BD bd = bdService.getById(bdCode);
            AssertUtil.isTrue(bd != null && bd.getDel() == EnableState.ENABLED, "营销员不存在，请检查营销员代码");
        }

        final String mobile = form.getMobile();
        AppUser appUser = this.findByMobile(form.getMobile(), accountType == AccountType.MASTER);
        if (appUser != null) {
            throw new AppBadRequestException("手机号已注册");
        }

        appUser = this.findByUsername(form.getUsername(), accountType == AccountType.MASTER);
        if (appUser != null) {
            throw new AppBadRequestException("用户名已注册");
        }

        appUser = new AppUser();
        appUser.setMobile(mobile);
        appUser.setUsername(form.getUsername());
        appUser.setPwd(form.getPassword());
        appUser.setMerchant(accountType);
        // set data
        appUser.setNickname(nickNameHasher.encode(System.currentTimeMillis()));
        appUser.setCreateTime(new Date());
        appUser.setBdCode(bdCode);
        appUser.setExpire(form.getExpire());

        appUser.insert();
        AppUserConfig config = new AppUserConfig();
        config.setUserId(appUser.getId());
        config.insert();
        return appUser;
    }

    @Override
    public AppUser registerSchoolUser(SchoolRegisterForm form) {
        AppUser appUser = this.findByUsername(form.getUsername(), true);
        if (appUser != null) {
            throw new AppBadRequestException("该用户名已注册");
        }

        appUser = new AppUser();
        BeanUtils.copyProperties(form, appUser);
        appUser.setMerchant(AccountType.MASTER);
        // set data
        appUser.setNickname(nickNameHasher.encode(System.currentTimeMillis()));
        appUser.setCreateTime(new Date());

        appUser.insert();
        return appUser;
    }

    /**
     * 后台管理
     * 用户列表
     */
    @Override
    public Page<UserManageDto> getUserListByManage(Page<UserManageDto> page, AppUserSearchForm form) {
        List<UserManageDto> records = baseMapper.getUserListByManage(page, form);
        page.setRecords(records);
        return page;
    }

    /**
     * 封禁或恢复用户
     *
     * @param userId
     * @param state
     */
    @Override
    public void blockOrRecover(Integer userId, AccountState state) {
        boolean b = baseMapper.blockOrRecover(userId, state);
        if (!b) {
            throw new AppBizException("操作异常！");
        }
    }
}
