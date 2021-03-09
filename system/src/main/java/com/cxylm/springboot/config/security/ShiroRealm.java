package com.cxylm.springboot.config.security;

import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.enums.AccountState;
import com.cxylm.springboot.model.system.User;
import com.cxylm.springboot.service.SysUserService;
import com.cxylm.springboot.util.ContextUtil;
import com.cxylm.springboot.util.JwtHelper;
import com.cxylm.springboot.util.RedisCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * ShiroRealm包含认证和授权两大模块
 *
 * @author Zhang Zhe
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private RedisCacheUtil redisService;
    @Autowired
    private JwtHelper jwtTokenUtil;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ShiroJWTToken;
    }

    /**
     * `
     * 授权模块，获取用户角色和权限
     *
     * @param token token
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
        Integer userId = jwtTokenUtil.getUserIdFromToken(token.toString());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色集
        // Set<String> roleSet = userManager.getUserRoles(username);
        Set<String> roleSet = sysUserService.getUserRoles(userId);
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限集
        // Set<String> permissionSet = userManager.getUserPermissions(username);
        Set<String> permissionSet = sysUserService.getUserPermissions(userId);
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param authenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的，已经经过了解密
        String token = (String) authenticationToken.getCredentials();

        // 从 redis里获取这个 token
        // HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // String ip = IPUtil.getIpAddr(request);
        final String ip = ContextUtil.getIPAddress();

        Integer userId = jwtTokenUtil.getUserIdFromToken(token);
        if (userId == null) throw new AuthenticationException("token校验不通过");

        // 张哲修改：这里不对token加密了，直接存
        // String encryptToken = FebsUtil.encryptToken(token);
//        String encryptToken = token;
        String currIP = (String) redisService.get(CacheName.SHIRO_TOKEN_PREFIX, String.valueOf(userId));
        // 如果找不到，说明已经失效
//        if (StringUtils.isBlank(currIP) || !ip.equals(currIP))
//            throw new AuthenticationException("Token expired, please re-login");

        // 通过用户名查询用户信息
        final User user = sysUserService.getById(userId);
        // User user = userManager.getUser(username);

        if (user == null) {
            throw new AuthenticationException("用户名或密码错误");
        }
        if (user.getState() != AccountState.NORMAL) {
            throw new AuthenticationException("账号已被锁定");
        }
//        if (!JWTUtil.verify(token, username, user.getPassword()))
//            throw new AuthenticationException("token校验不通过");
        return new SimpleAuthenticationInfo(token, token, "sys_user_shiro_realm");
    }
}
