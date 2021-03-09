package com.cxylm.springboot.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppSuccessResponse extends AppResponse {
    private static final int CODE_SUCCESS = 0;

    public AppSuccessResponse() {
        super(CODE_SUCCESS, "");
    }

    public AppSuccessResponse(Object data) {
        super(CODE_SUCCESS, "", data);
    }

    public AppSuccessResponse(String message) {
        super(CODE_SUCCESS, message, EMPTY_DATA);
    }

    public AppSuccessResponse(String message, Object data) {
        super(CODE_SUCCESS, message, data);
    }
}
