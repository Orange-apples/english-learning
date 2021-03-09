package com.cxylm.springboot.service;

import cn.hutool.core.collection.CollUtil;
import com.cxylm.springboot.config.property.AppProperty;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.dto.SMSMessage;
import com.cxylm.springboot.enums.SMSType;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.service.mq.MQService;
import com.cxylm.springboot.util.RedisCacheUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cxylm.springboot.config.QueueConfig.QUEUE_SMS;
import static com.cxylm.springboot.constant.AppMessage.ERROR_SMS_CODE_LOCKED;
import static com.cxylm.springboot.constant.Constant.SMS_TEMPLATE_MERCHANT_PASSED;
import static com.cxylm.springboot.constant.Constant.SMS_TEMPLATE_VERIFICATION_CODE;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SMSService {
    private static final String SMS_PREFIX = "【全民联盟】";

    private final RedissonClient redisson;
    private final RedisCacheUtil cacheUtil;
    private final AppProperty appProperty;
    private final MQService mqService;
    private final RedisTemplate<String, Object> redisTemplate;

    public String getSMSCode(String mobile) {
        return (String) cacheUtil.get(CacheName.SMS_CODE, mobile);
    }

    public String sendVerificationCode(String phone, Integer type) {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        if (this.checkFreq(phone)) {
            // 限频：1次/分钟
            throw new AppBadRequestException("获取验证码过于频繁，请1分钟后再试");
        }
        cacheUtil.set(CacheName.SMS_CODE, phone, code, appProperty.getSmsCodeExpire().getSeconds());
        cacheUtil.deleteFromHashMap(CacheName.SMS_CODE_ERROR_COUNT, phone);

        this.sendSMS(phone, getSMSCodeTemplate(type), new VerificationCodeSMSParam(code));
        return code;
    }

    public void sendPwd(String storeName, String username, String password, String mobile) {
        Map<String, Object> param = new HashMap<>();
        param.put("storeName", storeName);
        param.put("account", username);
        param.put("pwd", password);
        this.sendSMS(mobile, SMS_TEMPLATE_MERCHANT_PASSED, param);
    }

    public void sendSMS(String phone, String template, Object params) {
        SMSMessage sms = new SMSMessage();
        sms.phone = phone;
        sms.template = template;
        sms.params = params;
        mqService.sendMsgToMQ(QUEUE_SMS, sms);
    }

    public void sendSMSBatch(List<String> mobileList, String template, Object params) {
        // 批量发送短信
        final int size = mobileList.size();
        for (int i = 0; i < size; i += 998) {
            sendSMS(CollUtil.join(CollUtil.sub(mobileList, i, i + 998), ","), template, params);
        }
    }

    public void checkSMSCode(String phone, String codeToCheck) {
        final String code = this.getSMSCode(phone);
        if (code == null || !code.equals(codeToCheck)) {
            log.info("{}输入验证码{}错误", phone, codeToCheck);
            Long wrongTimes = cacheUtil.increaseMapValue(CacheName.SMS_CODE_ERROR_COUNT, phone, 1L);
            if (wrongTimes != null && wrongTimes > 5) {
                throw new AppBadRequestException(ERROR_SMS_CODE_LOCKED);
            }
            throw new AppBadRequestException("验证码错误");
        }
        cacheUtil.deleteFromHashMap(CacheName.SMS_CODE_ERROR_COUNT, phone);
    }

    public String getTemplate(SMSType smsType) {
        switch (smsType) {
            case SMS_CODE:
                return SMS_TEMPLATE_VERIFICATION_CODE;
            default:
                // 短信验证码模板
                return SMS_TEMPLATE_VERIFICATION_CODE;
        }
    }

    public boolean checkFreq(String phone) {
        long codeExpire = appProperty.getSmsCodeExpire().getSeconds();
        // 小于一分钟，则频率过快
        return codeExpire - cacheUtil.getTTLSeconds(CacheName.SMS_CODE, phone) < 60;
    }

    /**
     * 验证码验证成功后记录该用户验证状态
     *
     * @param phone
     * @param type
     */
    public void addSMSCodeState(String phone, SMSType type) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(CacheName.REDIS_KEY_FLAG + phone, type, appProperty.getSmsCodeExpire().getSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 校验redis中记录的验证码校验状态
     *
     * @param phone
     * @param type
     * @return
     */
    public void checkSMSCodeState(String phone, SMSType type) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Object o = ops.get(CacheName.REDIS_KEY_FLAG + phone);

        SMSType smsType = null;
        if (o != null) {
            smsType = SMSType.valueOfInt((Integer) o);
        }
        if (!type.equals(smsType)) {
            throw new AppBadRequestException("操作超时，请返回重新获取短信验证码");
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class VerificationCodeSMSParam implements Serializable {
        private static final long serialVersionUID = 8065825992559984617L;
        private String code;
    }

    private String getSMSCodeTemplate(Integer type) {
        switch (type) {
            case 0:
                return "SMS_187615152";
            case 1:
                return "SMS_187615154";
            case 2:
                return "SMS_187615151";
            default:
                return "SMS_187615152";
        }
    }
}
