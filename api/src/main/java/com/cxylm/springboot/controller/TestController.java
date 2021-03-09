package com.cxylm.springboot.controller;

import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.dto.WXPushMessage;
import com.cxylm.springboot.service.mq.MQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TestController extends ApiController {
    private final MQService mqService;

    @PublicAPI
    @GetMapping("/test-push")
    public ResponseEntity<?> pushTest(@RequestParam String openId) {
        WXPushMessage message = new WXPushMessage();
        message.openId = openId;
        message.text = "尊敬的家长您好，您的账号已开通xx课程，请积极监督孩子学习";
        mqService.sendMsgToMQ("cxylm.wx.push", message);
        return SUCCESS;
    }
}
