package com.cxylm.springboot.job;

import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.util.RedisCacheUtil;
import com.cxylm.springboot.util.WXUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class WxTokenJob extends QuartzJobBean {
    @Autowired
    private WXUtil wxUtil;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Refreshing wx token...");
        try {
            String accessToken = wxUtil.getAccessToken();
            if (accessToken == null) return;
            redisCacheUtil.set(CacheName.WX_ACCESS_TOKEN, accessToken);
        } catch (Exception e) {
            log.error("Error while refreshing wx token", e);
        }

        log.info("Refreshing baidu ai access token...");
        try {
            String accessToken = wxUtil.getBaiduToken();
            if (accessToken == null) return;
            redisCacheUtil.set(CacheName.BAIDU_AI_ACCESS_TOKEN, accessToken);
        } catch (Exception e) {
            log.error("Error while refreshing baidu ai access token", e);
        }
    }
}
