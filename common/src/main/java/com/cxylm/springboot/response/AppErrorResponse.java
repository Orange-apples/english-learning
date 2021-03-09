package com.cxylm.springboot.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppErrorResponse extends AppResponse {
    private static final int CODE_ERROR = -1;

    public AppErrorResponse() {
        super(CODE_ERROR, "");
    }

    public AppErrorResponse(int code, String message) {
        super(code, message);
    }

    public AppErrorResponse(String message) {
        super(CODE_ERROR, message);
    }

    public AppErrorResponse(String message, Object data) {
        super(CODE_ERROR, message, data);
    }
}
