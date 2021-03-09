package com.cxylm.springboot.config.interceptor;

import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.constant.ApiConstant;
import com.cxylm.springboot.exception.AppNoPermissionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MerchantApiInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isPublic = false;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 寻找PublicAPI注解，若包含，说明是公共接口，无需token即可访问
            PublicAPI annotation = method.getAnnotation(PublicAPI.class);
            isPublic = annotation != null;
        }

        if (isPublic) {
            return true;
        }

        final Integer storeId = (Integer) request.getAttribute(ApiConstant.REQUEST_ATTR_STORE_ID);
        if (storeId == null || storeId <= 0) {
            throw new AppNoPermissionException("您不是商家用户");
        }
        return true;
    }
}
