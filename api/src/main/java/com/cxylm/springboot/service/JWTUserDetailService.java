package com.cxylm.springboot.service;

import com.cxylm.springboot.model.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class JWTUserDetailService implements UserDetailsService {
    private final AppUserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("查找用户{}", s);
        final AppUser appUser = appUserService.findByMobile(s, false);
//        final AppUser appUser = appUserService.findByMobile(s);
//        if (appUser == null) {
//            throw new UsernameNotFoundException("用户名或密码错误");
//        }
        return User.builder().username(s)/*.password(appUser.getPassword())*/.roles("user").build();
        // return new AppUser(username, passwd, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
