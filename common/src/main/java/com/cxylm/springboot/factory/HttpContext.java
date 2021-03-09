package com.cxylm.springboot.factory;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpContext {
    public HttpContext() {
    }

    /**
     * 获取用户请求IP地址（由于nginx代理，因此需要从请求头中获得，而不是getRemoteHost方法）
     * @return
     */
    public static String getIp() {
        HttpServletRequest request = getRequest();
        return request == null ? "127.0.0.1" : request.getHeader("X-Forwarded-For");
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getResponse();
    }

    public static Map<String, String> getRequestParameters() {
        HashMap<String, String> values = new HashMap<>();
        HttpServletRequest request = getRequest();
        if (request == null) {
            return values;
        } else {
            Enumeration<String> enums = request.getParameterNames();

            while(enums.hasMoreElements()) {
                String paramName = enums.nextElement();
                String paramValue = request.getParameter(paramName);
                values.put(paramName, paramValue);
            }

            return values;
        }
    }
}
