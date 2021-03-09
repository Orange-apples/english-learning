package com.cxylm.springboot.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.constant.ApiConstant;
import com.cxylm.springboot.config.property.AppProperty;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.response.AppErrorResponse;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.AppUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ApiInterceptor extends HandlerInterceptorAdapter {
    private final AppProperty appProperty;
    private final AppUserService appUserService;

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

            //查看账号是否被禁用
            LambdaQueryWrapper<AppUser> queryWrapper = new LambdaQueryWrapper<AppUser>()
                    .select(AppUser::getAccountState)
                    .eq(AppUser::getId, userId);
            AppUser dbUser = appUserService.getOne(queryWrapper);
            if (AccountState.LOCKED.equals(dbUser.getAccountState())){
                return onAccountLocked(response);
            }

            request.setAttribute(ApiConstant.REQUEST_ATTR_USER_ID, Integer.parseInt(userId));
            // set store id
            request.setAttribute(ApiConstant.REQUEST_ATTR_STORE_ID, claims.get(ApiConstant.JWT_CLAIM_STORE_ID));
            request.setAttribute(ApiConstant.REQUEST_ATTR_ROLE, claims.get(ApiConstant.JWT_CLAIM_ROLE_NAME));
            return true;
        } else {
            return onAuthError(response);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 账号被禁用
     * @param response
     * @return
     * @throws IOException
     */
    private boolean onAccountLocked(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new AppErrorResponse(415, "账号已被禁用,请联系平台启用")));
        return false;
    }

    private boolean onAuthError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new AppErrorResponse(401, "会话过期，请重新登录")));
        return false;
    }
}
