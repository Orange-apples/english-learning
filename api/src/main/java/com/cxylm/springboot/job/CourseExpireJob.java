package com.cxylm.springboot.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxylm.springboot.model.StudyBookRate;
import com.cxylm.springboot.service.StudyBookRateService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

@Slf4j
public class CourseExpireJob extends QuartzJobBean {
    @Autowired
    private StudyBookRateService studyBookRateService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("[开始]扫描过期课程...");
        try {
            final List<StudyBookRate> studyBookRates = studyBookRateService.scanExpiredCourse();
            for (StudyBookRate studyBookRate : studyBookRates) {
                studyBookRateService.remove(new QueryWrapper<StudyBookRate>()
                        .eq("book_id", studyBookRate.getBookId())
                        .eq("user_id", studyBookRate.getUserId()));
            }
            log.info("[结束]扫描过期课程。共处理了{}个过期课程", studyBookRates.size());
        } catch (Exception e) {
            log.error("[异常中止]扫描过期课程：", e);
        }
    }
}
