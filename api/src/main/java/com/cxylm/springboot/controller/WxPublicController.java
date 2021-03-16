package com.cxylm.springboot.controller;

import com.cxylm.springboot.service.WxPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther Orange-apples
 * @date 2021/3/15 21:03
 * 微信公众号功能
 */
@RestController
@RequestMapping("/api/wxPublic")
public class WxPublicController {
    @Autowired
    WxPublicService wxPublicService;

    /**
     * 推送学习报告
     */
    @GetMapping("/pushReportAll")
    public ResponseEntity pushReportAll() {
        wxPublicService.pushReportAll();
        return ResponseEntity.ok("");
    }

    /**
     * 指定推送学习报告
     */
    @GetMapping("/pushReportByUserId")
    public ResponseEntity pushReportByUserId(Integer id) {
        wxPublicService.pushReportById(id);
        return ResponseEntity.ok("");
    }
}
