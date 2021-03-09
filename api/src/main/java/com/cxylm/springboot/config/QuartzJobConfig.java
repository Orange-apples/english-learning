package com.cxylm.springboot.config;

import com.cxylm.springboot.job.CourseExpireJob;
import com.cxylm.springboot.job.TimeJobExample;
import com.cxylm.springboot.job.WxTokenJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzJobConfig {
    @Bean
    public JobDetail timeJobDetail() {
        return JobBuilder.newJob(TimeJobExample.class)
                .withIdentity("TimeJobExample")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger timeJobTrigger(JobDetail timeJobDetail) {
        CronScheduleBuilder cronScheduleBuilder =
                CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?")
                        .withMisfireHandlingInstructionIgnoreMisfires();
        //SimpleScheduleBuilder simpleScheduleBuilder =
        //       SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(15).repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(timeJobDetail)
                .withIdentity("JobExampleTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail wxTokenJobDetail() {
        return JobBuilder.newJob(WxTokenJob.class)
                .withIdentity("wxTokenJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger wxTokenJobTrigger(JobDetail wxTokenJobDetail) {
        CronScheduleBuilder cronScheduleBuilder =
                CronScheduleBuilder.cronSchedule("0 0/45 * * * ?")
                        .withMisfireHandlingInstructionIgnoreMisfires();
        return TriggerBuilder.newTrigger()
                .forJob(wxTokenJobDetail)
                .withIdentity("wxTokenJobTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail courseExpireJobDetail() {
        return JobBuilder.newJob(CourseExpireJob.class)
                .withIdentity("CourseExpireJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger courseExpireJobTrigger(JobDetail courseExpireJobDetail) {
        CronScheduleBuilder cronScheduleBuilder =
                // 每日凌晨2点执行
                CronScheduleBuilder.cronSchedule("0 0 2 * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(courseExpireJobDetail)
                .withIdentity("courseExpireJobTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }
}
