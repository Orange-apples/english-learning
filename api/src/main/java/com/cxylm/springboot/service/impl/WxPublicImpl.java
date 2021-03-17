package com.cxylm.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.cxylm.springboot.dto.WXPushMessage;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.StudyTestRecords;
import com.cxylm.springboot.model.StudyWordRecords;
import com.cxylm.springboot.service.*;
import com.cxylm.springboot.service.mq.MQService;
import com.cxylm.springboot.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @auther Orange-apples
 * @date 2021/3/15 21:06
 */
@Service
@Slf4j
public class WxPublicImpl implements WxPublicService {
    @Autowired
    AppUserService appUserService;
    @Autowired
    StudyWordRecordsService studyWordRecordsService;
    @Autowired
    StudyTestRecordsService studyTestRecordsService;
    @Autowired
    MQService mqService;
    @Autowired
    LoginRecordService loginRecordService;

    //向所有用户推送
    public void pushReportAll() {
        List<AppUser> list = appUserService.list();
        for (AppUser appUser : list) {
            if (appUser.getAccountState().getValue().equals(AccountState.NORMAL.getValue())) {
                try {
                    pushReportById(appUser.getId());
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    public void pushReportById(Integer uid) {
        StringBuilder pusStr = new StringBuilder("尊敬的家长您好，您的账户【");
        AppUser user = appUserService.getById(uid);
        if (user == null) {
            throw new AppBadRequestException("未找到此用户");
        }
        if (StrUtil.isBlank(user.getWxOpenId())) {
            throw new AppBadRequestException("该账户未绑定微信无法推送");
        }
        pusStr.append(user.getUsername()).append("】\n");
        Date lastLoginTime = loginRecordService.getLastLoginTime(uid);
        pusStr.append("最后登录时间：").append(DateUtil.format(lastLoginTime, "yyyy-MM-dd HH:mm")).append("\n");
        Integer spellTime = user.getSpellTime();
        Integer learningTime = user.getLearningTime();
        Integer testTime = user.getTestTime();
        int i = (spellTime + learningTime + testTime) / 60;
        pusStr.append("累计学习时间：【").append(i).append("】").append("分钟\n");
        //昨日学习单词数量
        int count = studyWordRecordsService.count(new LambdaQueryWrapper<StudyWordRecords>()
                .eq(StudyWordRecords::getUserId, user.getId())
                .between(StudyWordRecords::getCreateTime, DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd 00:00:00"), DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd 23:59:59"))
        );
        pusStr.append("昨日学习单词数量【").append(count).append("】\n");

        List<StudyTestRecords> list = studyTestRecordsService.list(new LambdaQueryWrapper<StudyTestRecords>()
                .eq(StudyTestRecords::getUserId, user.getId())
                .between(StudyTestRecords::getCreateTime, DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd 00:00:00"), DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd 23:59:59"))
        );
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            pusStr.append("昨日测试次数【").append(size).append("】\n");
            int score = 0;
            for (StudyTestRecords studyTestRecords : list) {
                score += studyTestRecords.getScore();
            }
            pusStr.append("测试平均成绩【").append(score / size).append("】\n");
        }

        WXPushMessage message = new WXPushMessage();
        message.openId = user.getWxOpenId();
        message.text = pusStr.toString();
        mqService.sendMsgToMQ("cxylm.wx.push", message);
    }

    public static void main(String[] args) {
        System.out.println(10 / 3);
    }

}
