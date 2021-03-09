package com.cxylm.springboot.config;

import com.cxylm.springboot.config.security.ShiroProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "manager")
public class ManagerProperties {
    private ShiroProperties shiro = new ShiroProperties();
    private boolean openAopLog = true;
}
