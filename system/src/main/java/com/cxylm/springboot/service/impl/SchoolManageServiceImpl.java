package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.SchoolManageMapper;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.School;
import com.cxylm.springboot.service.SchoolManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SchoolManageServiceImpl extends ServiceImpl<SchoolManageMapper, School> implements SchoolManageService {
    @Override
    public void createSchool(String name, String addr) {
        School school = new School();
        school.setName(name);
        school.setAddr(addr);
        boolean b = school.insert();
        if(!b){
            throw new AppBizException("新建学区失败，请重新尝试！");
        }
    }

    @Override
    public void updateSchool(Integer schoolId, String name, String addr) {
        School school = new School();
        school.setId(schoolId);
        if(!StringUtils.isEmpty(name)){
            school.setName(name);
        }
        if(!StringUtils.isEmpty(addr)){
            school.setAddr(addr);
        }
        boolean b = school.updateById();
        if(!b){
            throw new AppBizException("更新学区失败，请重新尝试！");
        }
    }
}
