package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.model.CourseCard;

public interface CourseCardService extends IService<CourseCard> {
    /**
     * 将卡券的业务员绑定到用户上
     * @param cardId
     * @param userId
     */
    void bindBdToUser(Long cardId,Integer userId);

    boolean incUseCount(Long cardId);




}
