package com.cxylm.springboot.dto.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUpdateDto {
    private Integer minVersionCode;
    private String version;
    private Integer versionCode;
    private String url;
    private String log;
}
