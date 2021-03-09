package com.cxylm.springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParentsUserController extends ApiController {

    /**
     * 家长端用户 注册登录是否一套？？
     * 接口：
     * 1 登陆注册
     * 2 找回密码
     * 3 个人资料
     *
     */

    /**
     * 查询课程版本列表
     * @return
     */
    @GetMapping(value = "/bookVision")
    public ResponseEntity<?> bookVision(){
        return null;
    }

    /**
     * 查询课程列表
     * @return
     */
    @GetMapping(value = "/books")
    public ResponseEntity<?> courses(){
        return null;
    }

    /**
     * 查询课程详情
     * @return
     */
    @GetMapping(value = "/bookDetail")
    public ResponseEntity<?> bookDetail(){
        return null;
    }

    /**
     * 开通课程
     * @return
     */
    @PostMapping(value = "/books")
    public ResponseEntity<?> openCourses(){
        return null;
    }




}
