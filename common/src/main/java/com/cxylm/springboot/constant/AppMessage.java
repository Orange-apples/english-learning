package com.cxylm.springboot.constant;

public class AppMessage {
    public static final String ERROR_QUICK_LOGIN_FAIL = "登录失败，请切换至短信验证码登录";
    public static final String ERROR_LOGIN_FAIL = "用户名或密码错误";
    public static final String ERROR_USER_BLOCKED = "用户已被锁定";
    public static final String ERROR_SMS_CODE_LOCKED = "输入错误次数过多，请重新发送验证码";
    public static final String ERROR_OP_TOO_FREQUENT = "操作太快啦";

    public static final String ERROR_RECORD_NOT_EXIST = "记录不存在";
    public static final String ERROR_RECORD_OUTDATED = "记录已被更新，请刷新列表";

    public static final String ERROR_INVITE_VIP_ALREADY_JOINED = "已经加入了其他部落";
    public static final String ERROR_INVITE_VIP_NO_CHANCE = "您的邀请次数已用完";
    public static final String ERROR_NOT_VIP = "您当前不是会员";

    public static final String ERROR_BAD_REQUEST = "请求错误";
    public static final String ERROR_OP_FAIL = "操作失败";
    public static final String ERROR_ROB_ALREADY = "您已经抢过该红包了";

    public static final String ERROR_STORE_NOT_EXIST = "店铺不存在";
    public static final String ERROR_STORE_CLOSED = "店铺目前处于歇业状态，无法下单";
    public static final String ERROR_STORE_BLOCKED = "店铺已被封禁";
    public static final String ERROR_USER_NOT_EXIST = "用户不存在";
    public static final String ERROR_ORDER_NOT_EXIST = "订单不存在";
    public static final String ERROR_ORDER_EXPIRED = "订单已过期";
    public static final String ERROR_CODE_INVALID = "券码无效";

    public static final String ERROR_BALANCE_NOT_ENOUGH = "余额不足";


    public static final String ERROR_INVITE_CODE = "无效邀请码";


    public static final String ERROR_PAYMENT_ERROR = "支付系统异常";
    public static final String ERROR_ALREADY_PAID = "已经支付过了，请勿重复支付";

    public static final String ERROR_NOT_EXIST = "err.not-exist";

    public static final String ERROR_PAY_PWD_LOCKED = "密码连续错误次数过多，请重置密码后操作";
    public static final String ERROR_PWD_LOCKED = "密码连续错误次数过多，请采用验证码的方式重置密码后操作";
/*
    public static final String ERROR_RED_PACKET_ROBBED = "err.red-packet.robbed";
    public static final String ERROR_BAD_REQUEST = "err.bad-request";
    public static final String ERROR_NOT_EXIST = "err.not-exist";*/
}
