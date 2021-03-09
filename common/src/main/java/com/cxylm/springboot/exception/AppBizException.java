package com.cxylm.springboot.exception;

public class AppBizException extends RuntimeException {
    public AppBizException() {
        super("处理业务出错");
    }

    public AppBizException(String message) {
        super(message);
    }

}
