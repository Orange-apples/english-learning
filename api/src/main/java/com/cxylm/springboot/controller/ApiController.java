package com.cxylm.springboot.controller;

import com.cxylm.springboot.response.AppErrorResponse;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.response.AppSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.cxylm.springboot.constant.ApiConstant.*;

@Slf4j
public class ApiController {
    protected static ResponseEntity<AppResponse> BAD_REQUEST = ResponseEntity.badRequest().body(new AppErrorResponse("请求有误"));
    protected static ResponseEntity<AppResponse> SUCCESS = ResponseEntity.ok().body(new AppSuccessResponse());

    protected String getParam(String name) {
        return Objects.requireNonNull(getRequest()).getParameter(name);
    }

    protected void setAttr(String name, Object value) {
        Objects.requireNonNull(getRequest()).setAttribute(name, value);
    }

    protected Object getAttr(String name) {
        return Objects.requireNonNull(getRequest()).getAttribute(name);
    }

    protected String getHeader(String name) {
        return Objects.requireNonNull(getRequest()).getHeader(name);
    }

    protected Integer getUserId() {
        return (Integer) getAttr(REQUEST_ATTR_USER_ID);
    }

    protected String getUserName() {
        return (String) getAttr(REQUEST_ATTR_USERNAME);
    }

    protected String getIP() {
        HttpServletRequest request = getRequest();
        return request == null ? "127.0.0.1" : request.getHeader("X-Forwarded-For");
    }

    protected Integer getStoreId() {
        return (Integer) getAttr(REQUEST_ATTR_STORE_ID);
    }

    protected ResponseEntity createBadRequest(BindingResult result) {
        for (ObjectError error : result.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                return AppResponse.badRequest(fieldError.getDefaultMessage());
            }
        }
        return BAD_REQUEST;
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }
}
