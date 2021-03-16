package com.cxylm.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cxylm.springboot.dto.form.SchoolRegisterForm;
import com.cxylm.springboot.dto.form.SchoolSearchForm;
import com.cxylm.springboot.enums.AccountType;
import com.cxylm.springboot.enums.SchoolEnum;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.School;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.AppUserService;
import com.cxylm.springboot.service.SchoolManageService;
import com.cxylm.springboot.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/school")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SchoolManageController extends ApiController {

    private final SchoolManageService schoolManageService;
    private final AppUserService appUserService;

    @PostMapping
    @RequiresPermissions("school:add")
    public Object addWords(@RequestBody @Validated SchoolRegisterForm form) {
//        School school = new School();
//        BeanUtils.copyProperties(form, school);
//        school.insert();
        appUserService.registerSchoolUser(form);
        return SUCCESS;
    }

    @GetMapping("setState")
    @RequiresPermissions("school:update")
    public Object enableSchool(Integer id) {
        AppUser user = appUserService.getById(id);
        if (user == null) {
            throw new AppBadRequestException("未找到此记录");
        }
        user.setSchoolState(user.getSchoolState().equals(SchoolEnum.SCHOOL_ENABLE_0.getValue()) ? SchoolEnum.SCHOOL_ENABLE_1.getValue() : SchoolEnum.SCHOOL_ENABLE_0.getValue());
        appUserService.updateById(user);
        return SUCCESS;
    }

    @GetMapping
    @RequiresPermissions("school:view")
    public Object listSchool(SchoolSearchForm form) {
        final String name = form.getName();
        QueryWrapper<AppUser> wrapper = new QueryWrapper<>();
        wrapper.eq("merchant", AccountType.MASTER.getValue());
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("school_name", name);
        }
//        final IPage<School> list = schoolManageService.page(ApiPageFactory.getPage(), wrapper);
        final IPage<AppUser> list = appUserService.page(ApiPageFactory.getPage(), wrapper);
        return AppResponse.ok(list);
    }

    @PostMapping("/{id}/switch-state")
    @RequiresPermissions("school:update")
    public Object disableSchool(@PathVariable Integer id) {
        School school = schoolManageService.getById(id);
        if (school == null) {
            return AppResponse.badRequest("记录不存在");
        }
        school.setEnabled(!school.getEnabled());
        school.updateById();
        return SUCCESS;
    }

    @PutMapping("/{id}")
    @RequiresPermissions("school:update")
    public Object updateSchool(@RequestBody @Validated SchoolRegisterForm form, @PathVariable Integer id) {
//        School school = schoolManageService.getById(id);
//        if (school == null) {
//            return AppResponse.badRequest("记录不存在");
//        }
//        BeanUtils.copyProperties(form, school);
//        school.updateById();
        AppUser schoolUser = appUserService.getById(id);
        AssertUtil.isTrue(schoolUser != null && schoolUser.getMerchant() == AccountType.MASTER, "该校区账户不存在");
        schoolUser = new AppUser();
        schoolUser.setId(id);
        BeanUtils.copyProperties(form, schoolUser);
        appUserService.updateById(schoolUser);
        return SUCCESS;
    }
}
