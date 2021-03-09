package com.cxylm.springboot.config.security;

import lombok.Data;

@Data
public class ShiroProperties {
    private String anonUrls = "/api/manager/user/login";

    /**
     * token默认有效时间 1天
     */
    private Long jwtExpire = 86400L;
}
