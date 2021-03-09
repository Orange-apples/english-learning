package com.cxylm.springboot.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.cxylm.springboot.config.property.AppProperty;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.enums.CheckPayType;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.properties.SystemConfigProperties;
import com.cxylm.springboot.util.RedisCacheUtil;
import com.cxylm.springboot.util.rsa.RSAUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.cxylm.springboot.constant.AppMessage.ERROR_PAY_PWD_LOCKED;
import static com.cxylm.springboot.constant.AppMessage.ERROR_PWD_LOCKED;

/**
 * 密码相关操作
 *
 * @author HaoTi
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class PwdService {

    private final SystemConfigProperties systemConfigProperties;
    private final RedisCacheUtil cacheUtil;
    private final AppProperty appProperty;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * RSA解析用户密码，返回原文
     *
     * @param pwd
     * @return
     */
    public String getPwd(String pwd) {
        try {
            //获取平台秘钥
            String privateKey = systemConfigProperties.getRsaPriKey();
            //对传输过来的密码进行RSA解密
            pwd = pwd.replaceAll(" ", "+");
            pwd = RSAUtils.decryptDataOnJava(pwd, privateKey);
        } catch (Exception e) {
            log.info("密码解析错误!");
            throw new AppBadRequestException("密码错误!");
        }
        return pwd;
    }

    /**
     * RSA解析用户密码，返回MD5密文
     *
     * @param pwd 密文
     * @return
     */
    public String getPwdToMD5(String pwd) {
        try {
            //获取平台秘钥
            String privateKey = systemConfigProperties.getRsaPriKey();
            //对传输过来的密码进行RSA解密
            pwd = pwd.replaceAll(" ", "+");
            pwd = RSAUtils.decryptDataOnJava(pwd, privateKey);
        } catch (Exception e) {
            log.info("密码解析错误!");
            throw new AppBadRequestException("密码错误!");
        }
        return DigestUtil.md5Hex(pwd);
    }

    /**
     * 用户支付时校验支付密码是否正确
     *
     * @param userId     用户
     * @param pwdToCheck 用户输入的密码密文
     * @param pwdPay     正确密码
     */
    public void checkPayPwd(Integer userId, String pwdToCheck, String pwdPay) {
        // 先判断是否输入错误超过5次
        Integer wrongTimes = (Integer) cacheUtil.getValueFromHashMap(CacheName.PAY_PWD_ERROR_COUNT, userId.toString());

        wrongTimes = wrongTimes == null ? 0 : wrongTimes;
        if (wrongTimes > 5) {
            throw new AppBadRequestException(ERROR_PAY_PWD_LOCKED);
        }

        final boolean passwordCorrect = checkRSAPassword(pwdToCheck, pwdPay);
        if (passwordCorrect) {
            if (wrongTimes > 0) {
                cacheUtil.deleteFromHashMap(CacheName.PAY_PWD_ERROR_COUNT, userId.toString());
            }
        } else {
            cacheUtil.increaseMapValue(CacheName.PAY_PWD_ERROR_COUNT, userId.toString(), 1L);
            throw new AppBadRequestException("支付密码错误");
        }
    }

    public void checkLoginPwd(Integer userId, String pwdToCheck, String pwd) {
        // 先判断是否输入错误超过5次
        Integer wrongTimes = (Integer) cacheUtil.getValueFromHashMap(CacheName.LOGIN_ERROR_COUNT, userId.toString());

        wrongTimes = wrongTimes == null ? 0 : wrongTimes;
        if (wrongTimes > 5) {
            throw new AppBadRequestException(ERROR_PWD_LOCKED);
        }

        final boolean passwordCorrect = checkRSAPassword(pwdToCheck, pwd);
        if (passwordCorrect) {
            if (wrongTimes > 0) {
                cacheUtil.deleteFromHashMap(CacheName.LOGIN_ERROR_COUNT, userId.toString());
            }
        } else {
            cacheUtil.increaseMapValue(CacheName.LOGIN_ERROR_COUNT, userId.toString(), 1L);
            throw new AppBadRequestException("用户名或密码错误");
        }
    }

    private boolean checkRSAPassword(String pwdToCheck, String pwd) {
        try {
            //获取平台秘钥
            String privateKey = systemConfigProperties.getRsaPriKey();
            //对传输过来的密码进行RSA解密
            pwdToCheck = pwdToCheck.replaceAll(" ", "+");
            pwdToCheck = RSAUtils.decryptDataOnJava(pwdToCheck, privateKey);
        } catch (Exception e) {
            log.info("密码解析错误!", e);
            return false;
        }

        //MD5加密
        pwdToCheck = DigestUtil.md5Hex(pwdToCheck);
        return pwdToCheck.equals(pwd);
    }


    /**
     * 支付密码验证成功后记录该用户验证状态
     *
     * @param userId
     * @param type
     */
    public void addPayPwdState(String userId, CheckPayType type) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(CacheName.REDIS_KEY_PAY_PWD_FLAG + userId, type, appProperty.getSmsCodeExpire().getSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 校验redis中记录的验证码校验状态
     *
     * @param userId
     * @param type
     * @return
     */
    public void checkPayPwdState(String userId, CheckPayType type) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        CheckPayType payType = (CheckPayType) ops.get(CacheName.REDIS_KEY_PAY_PWD_FLAG + userId);
        if (type != payType) {
            throw new AppBadRequestException("密码设置失败！");
        }
    }

    /**
     * 解析新密码和旧密码并判断旧密码是否正确
     *
     * @param userId
     * @param appUserPwd
     * @param oldPwd
     * @param pwd
     * @return
     */
    public String getAndCheckPwd(Integer userId, String appUserPwd, String oldPwd, String pwd) {
        // 先判断是否输入错误超过5次
        Integer wrongTimes = (Integer) cacheUtil.getValueFromHashMap(CacheName.LOGIN_ERROR_COUNT, userId.toString());

        wrongTimes = wrongTimes == null ? 0 : wrongTimes;
        if (wrongTimes > 5) {
            throw new AppBadRequestException(ERROR_PWD_LOCKED);
        }

        try {
            //获取平台秘钥
            String privateKey = systemConfigProperties.getRsaPriKey();
            //对传输过来的密码进行RSA解密
            oldPwd = oldPwd.replaceAll(" ", "+");
            oldPwd = RSAUtils.decryptDataOnJava(oldPwd, privateKey);
            pwd = pwd.replaceAll(" ", "+");
            pwd = RSAUtils.decryptDataOnJava(pwd, privateKey);
        } catch (Exception e) {
            log.info("密码解析错误!");
            throw new AppBadRequestException("密码错误!");
        }
        //MD5加密
        oldPwd = DigestUtil.md5Hex(oldPwd);
        pwd = DigestUtil.md5Hex(pwd);
        if (!appUserPwd.equals(oldPwd)) {
            //密码输入错误，加入到redis
            cacheUtil.increaseMapValue(CacheName.LOGIN_ERROR_COUNT, userId.toString(), 1L);
            throw new AppBadRequestException("原密码错误！");
        }
        if (wrongTimes > 0) {
            cacheUtil.deleteFromHashMap(CacheName.LOGIN_ERROR_COUNT, userId.toString());
        }
        return pwd;
    }
}
