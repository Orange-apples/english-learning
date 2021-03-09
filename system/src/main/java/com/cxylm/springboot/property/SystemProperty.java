package com.cxylm.springboot.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "system")
public class SystemProperty {
    private String jwtSecret = "GgbL6vypKjo7ODCuky1FDgbhtVW4XqLFlvKJmuSbWkm4CqnfEsV2Iq";
    private String flash253Api = "https://api.253.com/open/flashsdk";
    private String flash253AppKey = "";
}
