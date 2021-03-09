package com.cxylm.springboot.service.impl;

import com.cxylm.springboot.constant.Constant;
import com.cxylm.springboot.enums.AccountType;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.model.AppUserConfig;
import com.cxylm.springboot.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AppUserRegService {
    private Hashids nickNameHasher = new Hashids(Constant.NICKNAME_HASH_SALT, 4);

    private final SMSService smsService;

    @Transactional
    public AppUser registerUser(String mobile, String username, String plainPassword, AccountType accountType) {
        AppUser appUser = new AppUser();
        appUser.setMobile(mobile);
        appUser.setUsername(username);
        appUser.setPwd(plainPassword);
        appUser.setMerchant(accountType);
        // set data
        appUser.setNickname(nickNameHasher.encode(System.currentTimeMillis()));
        appUser.setCreateTime(new Date());
//        appUser.setAccount(mobile);
//        appUser.setStoreId(0);

        appUser.insert();

        AppUserConfig config = new AppUserConfig();
        config.setUserId(appUser.getId());
        config.insert();

        //插入用户钱包表
//        UserWallet userWalle = new UserWallet();
//        Integer userId = appUser.getId();
//        userWalle.setUserId(userId);
//        userWalle.insert();

        return appUser;
    }
}
