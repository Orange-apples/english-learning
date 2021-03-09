package com.cxylm.springboot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cxylm.system-config")
public class SystemConfigProperties {
//    /** 图片配置 */
//    private String trackerServers;
    private String zimgAccess;
//    private String fastDFSAccess;

    /**用户版RSA**/
    private String rsaPriKey;
    private String rsaKey;
//    /**商家版RSA**/
//    private String mchRsaPriKey;
//    private String mchRsaKey;
//
//    //客服电话
    private String phone;
//    private String desKey;

    private Float withdrawRate = 6.0f;
}
