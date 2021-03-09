package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.CourseCardMapper;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.CourseCard;
import com.cxylm.springboot.service.AppUserService;
import com.cxylm.springboot.service.CourseCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CourseCardServiceImpl extends ServiceImpl<CourseCardMapper, CourseCard> implements CourseCardService {

    private final AppUserService appUserService;
    @Override
    public void bindBdToUser(Long cardId, Integer userId) {
        AppUser appUser = appUserService.getById(userId);
        //判断是否已经绑定业务员
        if (appUser.getBdCode() == null) {
            CourseCard courseCard = super.getById(cardId);
            //卡券的业务员绑定到用户中
            if (courseCard.getSysBdId() != null) {
                appUser.setBdCode(courseCard.getSysBdId());
                appUserService.updateById(appUser);
            }
        }

    }

    @Override
    public boolean incUseCount(Long cardId) {
        return baseMapper.incUseCount(cardId);
    }
}
