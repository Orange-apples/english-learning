package com.cxylm.springboot.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

@Data
@AllArgsConstructor
public class ShiroJWTToken implements AuthenticationToken {
    private static final long serialVersionUID = -2552318658695271099L;

    private String token;
    private String expireAt;

    public ShiroJWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
