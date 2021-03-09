package com.cxylm.springboot.config;

import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.config.property.AppProperty;
import com.cxylm.springboot.constant.ApiConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ManagerApiInterceptor extends HandlerInterceptorAdapter {
    private final AppProperty appProperty;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

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

        final String authHeader = request.getHeader(ApiConstant.AUTH_HEADER_NAME);
        if (authHeader != null && authHeader.startsWith(ApiConstant.AUTH_HEADER_VALUE_PREFIX)) {
            String token = authHeader.substring(7);
            final Claims claims;
            try {
                claims = Jwts.parser().setSigningKey(appProperty.getJwtSecret()).parseClaimsJws(token).getBody();
            } catch (Exception e) {
                return onAuthError(response);
                //throw new AuthException("Invalid token");
            }

            if (claims.getExpiration().before(new Date())) {
                return onAuthError(response);
                //throw new AuthException("Invalid token");
            }
            final String userId = claims.getSubject();
            request.setAttribute(ApiConstant.REQUEST_ATTR_USER_ID, Integer.parseInt(userId));
            // set role
            final String username = (String) claims.get(ApiConstant.JWT_CLAIM_USERNAME);
            if (username == null) {
                return onAuthError(response);
            }
            request.setAttribute(ApiConstant.REQUEST_ATTR_USERNAME, username);
            return true;
        } else {
            return onAuthError(response);
        }
    }

    private boolean onAuthError(HttpServletResponse response) {
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        try {
            response.sendError(HttpStatus.SC_UNAUTHORIZED, "");
        } catch (IOException e) {
            log.error("Error in response", e);
            throw new RuntimeException(e);
        }
        return false;
    }
}
