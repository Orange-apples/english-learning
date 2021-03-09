package com.cxylm.springboot.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import com.cxylm.springboot.config.property.AppProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {
    @Bean
    public WebClient commonWebClient() {
        return WebClient.builder().build();
    }


//    @Bean
    public JPushClient jPushClient(AppProperty appProperty) {
        return new JPushClient(
                appProperty.getJPushMasterSecret(), appProperty.getJPushAppKey(), null,
                ClientConfig.getInstance());
    }
}
