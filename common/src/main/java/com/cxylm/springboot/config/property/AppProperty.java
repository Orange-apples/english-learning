package com.cxylm.springboot.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperty {
    private String jwtSecret = "qBWxL951Kva63rkzln6h8PSLLCcC8YJoMqjXOUaZVizwSJsl5enU5L";

    private Duration smsCodeExpire = Duration.ofMinutes(5);

    private String aliAccessKey = "";
    private String aliSecret = "";
    private String wxAppId = "wx6f4b790de6421c79";
    private String wxSecret = "277b72a98e889a30982902a34076ac2c";

    private String baiduAppId = "20398082";
    private String baiduApiKey = "bGvCCTS5bGWj3Bc0rZx4C2IN";
    private String baiduApiSecret = "kBskphhUpt6a3aRIdS7mUX6CGzxChGRz";

    private String jPushMasterSecret;
    private String jPushAppKey;
}
