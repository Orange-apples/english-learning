package com.cxylm.springboot.controller;

import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.dto.form.SMSCodeForm;
import com.cxylm.springboot.service.SMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysController extends ApiController {
    private final SMSService smsService;

    @PublicAPI
    @PostMapping("/sms/code")
    public Object selectBookDetail(@RequestBody @Validated SMSCodeForm form){
        smsService.sendVerificationCode(form.getMobile(), form.getType());
        return SUCCESS;
    }
}
