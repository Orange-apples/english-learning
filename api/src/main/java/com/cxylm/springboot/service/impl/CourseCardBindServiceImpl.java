package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.CourseCardBindMapper;
import com.cxylm.springboot.model.CourseCardBind;
import com.cxylm.springboot.service.CourseCardBindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CourseCardBindServiceImpl extends ServiceImpl<CourseCardBindMapper, CourseCardBind> implements CourseCardBindService {
    @Override
    public boolean isBind(int courseId, long cardId) {
        return baseMapper.isBind(courseId, cardId);
    }

    @Override
    public boolean bindUser(int userId, int bookId, long cardId) {
        return baseMapper.bindUser(userId, bookId, cardId) > 0;
    }
}
