package com.cxylm.springboot.util;

import com.cxylm.springboot.systemdto.SysUserContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ContextUtil {
    public static SysUserContext getSysUser() {
        return (SysUserContext) getRequest().getAttribute("sys-user");
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    public static String getIPAddress() {
        return getRequest().getHeader("X-Forwarded-For");
    }
}
