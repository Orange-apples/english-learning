package com.cxylm.springboot;

import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.util.RedisCacheUtil;
import com.cxylm.springboot.util.WXUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(99)
@Slf4j
public class StartupRunner implements CommandLineRunner {

    private final WXUtil wxUtil;
    private final RedisCacheUtil cacheUtil;

    public StartupRunner(WXUtil wxUtil, RedisCacheUtil cacheUtil) {
        this.wxUtil = wxUtil;
        this.cacheUtil = cacheUtil;
    }

    @Override
    public void run(String... args) throws Exception {
        String accessToken = wxUtil.getAccessToken();
        if (accessToken != null) {
            cacheUtil.set(CacheName.WX_ACCESS_TOKEN, accessToken);
        } else {
            log.warn("Get wx token failed");
        }

        accessToken = wxUtil.getBaiduToken();
        if (accessToken != null) {
            cacheUtil.set(CacheName.BAIDU_AI_ACCESS_TOKEN, accessToken);
        } else {
            log.warn("Get baidu token failed");
        }

        log.info("START UP RUNNER JOB DONE.");
    }
}
