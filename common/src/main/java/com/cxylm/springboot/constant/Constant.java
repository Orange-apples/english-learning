package com.cxylm.springboot.constant;

import java.io.File;

public final class Constant {

    // 推送队列
    public final static String QUEUE_PUSH ="cxylm.push";
    // 订单过期队列名
    public final static String QUEUE_ORDER_EXPIRE ="cxylm.order.expire";
    // 订单过期路由键
    public final static String ROUTING_KEY_ORDER_EXPIRE ="order.expire";

    //微信支付
    // APP支付
    public final static String TRADE_TYPE_APP="APP";
    public final static String TRADE_TYPE_JSPAI = "JSAPI";

    //回调通知
    public static final String RETURN_ALIPAY_VALUE_SUCCESS = "success";
    public static final String RETURN_ALIPAY_VALUE_FAIL = "fail";

    public static final String RETURN_VALUE_SUCCESS = "SUCCESS";
    public static final String RETURN_VALUE_FAIL = "FAIL";

    // TODO 更改模板
    /** 短信模板：验证码 */
    public static final String SMS_TEMPLATE_VERIFICATION_CODE = "SMS_XXXXXXXXXX";
    public static final String SMS_TEMPLATE_MEMBER_EXPIRED_NOTICE = "SMS_XXXXXXXXXX";

    // 商家默认密码短信短信模板
    public static final String SMS_TEMPLATE_MERCHANT_PASSED = "SMS_XXXXXXXXXX";
    public static final String MERCHANT_DEFAULT_PWD = "888888";


    public static class AlipayConstant {
        public final static String CONFIG_PATH = "alipay" + File.separator + "alipay";	// 支付宝移动支付
        public final static String TRADE_STATUS_WAIT = "WAIT_BUYER_PAY";		// 交易创建,等待买家付款
        public final static String TRADE_STATUS_CLOSED = "TRADE_CLOSED";		// 交易关闭
        public final static String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";		// 交易成功
        public final static String TRADE_STATUS_FINISHED = "TRADE_FINISHED";	// 交易成功且结束
    }

    public static final String NICKNAME_HASH_SALT= "hmX&pYRP*Jo";

    public static final String DEFAULT_ICON = "9ce4f9b6abd139adb3f1fd813951563c";

    public static final long SUPER_ADMIN_ROLE_ID = 0;
}
