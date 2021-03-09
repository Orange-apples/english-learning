package com.cxylm.springboot.util;

import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.exception.AppBizException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssertUtil {
    /**
     * 验证非null，否则抛出请求错误异常
     */
    public static void badRequestWhenNull(Object object) {
        badRequestWhenNull(object, "请求错误");
    }

    /**
     * 验证非null，否则抛出请求错误异常
     */
    public static void badRequestWhenNull(Object object, String message) {
        if (object == null) {
            throw new AppBadRequestException(message);
        }
    }

    public static void bizErrorWhenNull(Object object) {
        if (object == null) {
            throw new AppBizException();
        }
    }

    public static void bizErrorWhenNull(Object object, String message) {
        if (object == null) {
            log.error(message);
            throw new AppBizException();
        }
    }

    public static void isTrue(boolean b, String message) {
        if (!b) {
            throw new AppBadRequestException(message);
        }
    }

    public static void isTrue(boolean b) {
        isTrue(b, "数据校验失败");
    }

    public static void isFalse(boolean b, String msg) {
        isTrue(!b, msg);
    }
}
