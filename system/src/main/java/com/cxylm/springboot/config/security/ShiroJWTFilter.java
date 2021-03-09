package com.cxylm.springboot.config.security;

import com.cxylm.springboot.constant.ApiConstant;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
//@Component
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShiroJWTFilter extends BasicHttpAuthenticationFilter {
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final String[] anonUrls = new String[]{"/api/manager/user/login"};
//    @Autowired
//    private ManagerProperties managerProperties;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        String[] anonUrl = StringUtils.splitByWholeSeparatorPreserveAllTokens(managerProperties.getShiro().getAnonUrls(), COMMA);

        final String requestURI = httpServletRequest.getRequestURI();
        boolean match = false;
        for (String u : anonUrls) {
            if (pathMatcher.match(u, requestURI)) {
                match = true;
                break;
            }
        }
        if (match) return true;
        if (isLoginAttempt(request, response)) {
            return executeLogin(request, response);
        }
        return false;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(ApiConstant.AUTH_HEADER_NAME);
        return token != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(ApiConstant.AUTH_HEADER_NAME);
        if (token == null || !token.startsWith(ApiConstant.AUTH_HEADER_VALUE_PREFIX)) {
            return false;
        }
        // 张哲修改：这里不对token加密了，直接存
        // ShiroJWTToken jwtToken = new ShiroJWTToken(FebsUtil.decryptToken(token));
        ShiroJWTToken jwtToken = new ShiroJWTToken(token.substring(7));
        try {
            getSubject(request, response).login(jwtToken);
            return true;
        } catch (AuthenticationException | JwtException e) {
            log.info("[Shiro] Auth error: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("[Shiro] Error while executeLogin:", e);
            return false;
        }
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个 option请求，这里我们给 option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        /*httpResponse.setCharacterEncoding("utf-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        final String message = "403 forbidden";
        try (PrintWriter out = httpResponse.getWriter()) {
            String responseJson = "{\"message\":\"" + message + "\"}";
            out.print(responseJson);
        } catch (IOException e) {
            log.error("sendChallenge error：", e);
        }*/
        return false;
    }
}
