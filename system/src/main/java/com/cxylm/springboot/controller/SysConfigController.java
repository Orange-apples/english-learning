package com.cxylm.springboot.controller;

import com.cxylm.springboot.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @auther Orange-apples
 * @date 2021/3/15 22:47
 */
@RestController
@RequestMapping("api/sysConfig")
public class SysConfigController {
    @Autowired
    SysConfigService sysConfigService;
    @PostMapping("set")
    public ResponseEntity set(String jsonStr){
        sysConfigService.set(jsonStr);
        return ResponseEntity.ok("");
    }
    @GetMapping("get")
    public ResponseEntity get(){
        return ResponseEntity.ok(sysConfigService.get());
    }
}
