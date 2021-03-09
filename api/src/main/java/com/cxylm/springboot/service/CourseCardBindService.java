package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.model.CourseCardBind;

public interface CourseCardBindService extends IService<CourseCardBind> {
    boolean isBind(int courseId, long cardId);
    boolean bindUser(int userId, int bookId, long cardId);
}
