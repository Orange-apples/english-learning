package com.cxylm.springboot.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class AppResponse {
    protected static final int CODE_SUCCESS = 0;
    protected static final Map<String, Object> EMPTY_DATA = new HashMap<>(2);

    private int code;
    private String message;
    private Object data;

    public AppResponse() {
        this(CODE_SUCCESS, "", EMPTY_DATA);
    }

    public AppResponse(Object data) {
        this(CODE_SUCCESS, "", data);
    }

    public AppResponse(int code, String message) {
        this(code, message, EMPTY_DATA);
    }

    public static ResponseEntity<?> ok() {
        return ResponseEntity.ok().body(new AppSuccessResponse());
    }

    public static ResponseEntity<?> ok(Object data) {
        return ResponseEntity.ok(new AppSuccessResponse(data));
    }

    public static ResponseEntity<?> ok(String message, Object data) {
        return ResponseEntity.ok(new AppSuccessResponse(message, data));
    }

    public static ResponseEntity<?> badRequest(String message) {
        return ResponseEntity.badRequest().body(new AppErrorResponse(message));
    }

    public static ResponseEntity<?> badRequest() {
        return ResponseEntity.badRequest().body(new AppErrorResponse("请求有误"));
    }

    public static ResponseEntity<?> badRequest(String message, Object data) {
        return ResponseEntity.badRequest().body(new AppErrorResponse(message, data));
    }

    /**
     * 用户账号被禁用返回的状态码
     * @return
     */
    public static ResponseEntity<?> accountLocked(){
        return new ResponseEntity<>(new AppErrorResponse("账号已被禁用,请联系平台启用"), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
