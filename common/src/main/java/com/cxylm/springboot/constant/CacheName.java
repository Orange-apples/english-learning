package com.cxylm.springboot.constant;

public class CacheName {
    public static final String APP_USER = "AppUser";

    public static final String SMS_CODE_ERROR_COUNT = "SMS-ErrorCount";
    public static final String PAY_PWD_ERROR_COUNT = "PayPwd-ErrorCount";
    public static final String LOGIN_ERROR_COUNT = "Login-ErrorCount";

    public static final String SMS_CODE = "SMS-Code";

    /** 商家收款语音播报提醒 */
    public static final String NOTICE_RECEIPT = "Notice-Receipt:";
    public static final String USER_NOTICE = "User-Notice:";

    public static final String MEMBER_DAILY_INC_COUNT = "MemberDailyIncCount";
    public static final String TEAM_DAILY_INCOME_COUNT = "TeamDailyIncomeCount";


    public static final String REDIS_KEY_FLAG = "verificationCodeFlag::";
    public static final String REDIS_KEY_PAY_PWD_FLAG = "verificationPayPewdFlag::";

    public static final String CHECK_SMS_POST = "CheckSmsPost";
    public static final String APP_UPDATE = "AppUpdate";
    public static final String APP_USER_LOCK_NAME = "AppUserLock:";

    public static final String COURSE_LOCK = "CourseLock:";
    public static final String CARD_BATCH_LOCK = "CardBatchLock";

    public static final String SHIRO_TOKEN_PREFIX = "ShiroToken:";
    public static final String SYS_USER = "SysUser";
    public static final String SYS_USER_ROLE = "SysUserRole";
    public static final String SYS_USER_PERMISSION = "SysUserPermission";
    public static final String WX_ACCESS_TOKEN = "WxAccessToken";
    public static final String BAIDU_AI_ACCESS_TOKEN = "BaiduAIAccessToken";
}
