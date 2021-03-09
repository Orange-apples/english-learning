package com.cxylm.springboot.config;

import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.exception.AppNoPermissionException;
import com.cxylm.springboot.exception.AuthException;
import com.cxylm.springboot.response.AppErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * 拦截登录授权异常
     */
    @ExceptionHandler(AuthException.class)
    public Object onAuthError(AuthException e) {
        log.warn("Auth exception occurred. {} ", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        return ResponseEntity.status(HttpStatus.OK).body(new AppErrorResponse(401, "会话已过期请重新登录"));
    }

    @ExceptionHandler(AppNoPermissionException.class)
    public Object onAuthError(AppNoPermissionException e) {
        String message = "无权限";
        if (e.getMessage() != null) {
            message = e.getMessage();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AppErrorResponse(message));
    }

    /**
     * 请求异常
     */
    @ExceptionHandler(AppBadRequestException.class)
    public Object onBadRequest(AppBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppErrorResponse(e.getMessage()));
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        return handleBadRequest(ex.getBindingResult());
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleBindException(@NotNull BindException ex, @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        return handleBadRequest(ex.getBindingResult());
    }

    @NotNull
    private ResponseEntity<Object> handleBadRequest(BindingResult bindingResult) {
        String message = "请求有误";
        final FieldError fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
            log.warn("请求有误: {}", message);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppErrorResponse(message));
    }

    @ExceptionHandler(AppBizException.class)
    public Object onBizError(AppBizException e) {
        log.error("业务异常", e);
        String message = "业务异常";
        if (e.getMessage() != null) {
            message = e.getMessage();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AppErrorResponse(message));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Object onSysUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AppErrorResponse("403 无权限"));
    }

    @ExceptionHandler(RuntimeException.class)
    public Object onServerError(RuntimeException e) {
        log.error("服务器异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AppErrorResponse("服务器异常"));
    }
}
