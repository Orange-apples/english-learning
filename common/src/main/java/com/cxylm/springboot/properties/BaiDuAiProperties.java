package com.cxylm.springboot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 百度AI配置(如果需要配置别的配置可参照这个形式自己添加)
 *
 * @author HaoTi
 * @date 2019-01-09 20:37
 */

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cxylm.baidu-ai")
public class BaiDuAiProperties {
    /** 限定包名 */
    private String appId;

    private String apiKey;
    private String secretKey;
}
