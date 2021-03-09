package com.cxylm.springboot.exception;

public class AuthException extends RuntimeException {
    private static final long serialVersionUID = -5404461060146606958L;

    public AuthException() {
        super();
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }
}
