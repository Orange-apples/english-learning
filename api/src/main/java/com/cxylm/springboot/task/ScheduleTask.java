package com.cxylm.springboot.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jpush.api.schedule.ScheduleClient;
import com.cxylm.springboot.constant.SysConfigConstants;
import com.cxylm.springboot.enums.SysConfigEnum;
import com.cxylm.springboot.service.SysConfigService;
import com.cxylm.springboot.service.WxPublicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @auther Orange-apples
 * @date 2021/3/15 21:45
 */
@Component
@Slf4j
public class ScheduleTask implements ApplicationRunner {
    @Autowired
    ScheduledExecutorService scheduledExecutorService;
    @Autowired
    SysConfigService sysConfigService;
    @Autowired
    WxPublicService wxPublicService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Thread thread = new Thread(() -> {
            log.info("===定时推送学习报告任务初始化===");
            while (true) {
                String enable = sysConfigService.getConfig(SysConfigConstants.PUSH_ENABLE);
                if (!enable.equals(SysConfigEnum.PUSH_ENABLE_1.getValue())) {
                    //推送未开启
                    return;
                }
                String value = sysConfigService.getConfig(SysConfigConstants.PUSH_TIME);
                if (StrUtil.isBlank(value)) {
                    return;
                }
                String format = DateUtil.format(DateUtil.tomorrow(), "yyyy-MM-dd");
                String time = format + " " + value;
                DateTime t = DateUtil.parse(time);
                long l = t.toTimestamp().getTime() - new Date().getTime();
                log.info("==线程休眠时间{}==", l);
                try {
                    Thread.sleep(l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("===定时推送学习报告任务开始===");
                wxPublicService.pushReportAll();
            }
        });
        thread.start();
    }
}
