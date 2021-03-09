package com.cxylm.springboot.exception;

public class AppBadRequestException extends RuntimeException {
    public AppBadRequestException() {
        super("请求有误");
    }

    public AppBadRequestException(String message) {
        super(message);
    }

}
