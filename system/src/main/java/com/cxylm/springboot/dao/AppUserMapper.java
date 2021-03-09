package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.UserManageDto;
import com.cxylm.springboot.dto.form.AppUserSearchForm;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.model.AppUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AppUserMapper extends BaseMapper<AppUser> {
    AppUser findByMobile(@Param("mobile") String mobile);

    @Select("SELECT * FROM `app_user` ORDER BY id DESC LIMIT 1")
    AppUser findLast();

    /**
     * 后台管理
     * 用户列表
     *
     * @return
     */
    List<UserManageDto> getUserListByManage(@Param("page") Page<UserManageDto> page, @Param("searchForm") AppUserSearchForm searchForm);

    /**
     * 封禁或恢复用户
     *
     * @param userId
     * @param state
     * @return
     */
    boolean blockOrRecover(@Param("userId") Integer userId, @Param("state") AccountState state);
}
