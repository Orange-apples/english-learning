package com.cxylm.springboot.systemdto;

import lombok.Data;

@Data
public class SysUserContext {
    private Long userId;
    private String username;
    private String role;
}
