package com.cxylm.springboot.exception;

public class AppNoPermissionException extends RuntimeException {

    private static final long serialVersionUID = 9172517328238997284L;

    public AppNoPermissionException() {
        super();
    }

    public AppNoPermissionException(String message) {
        super(message);
    }
}
