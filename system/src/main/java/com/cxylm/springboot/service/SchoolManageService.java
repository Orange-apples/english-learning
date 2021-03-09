package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.model.School;

/**
 * 学区管理
 */
public interface SchoolManageService extends IService<School> {
    void createSchool(String name, String addr);

    void updateSchool(Integer schoolId, String name, String addr);
}
