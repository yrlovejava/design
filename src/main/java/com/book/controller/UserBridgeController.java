package com.book.controller;

import com.book.pojo.UserInfo;
import com.book.service.UserBridgeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 桥接模式用户注册登录控制器
 */
@RestController
@RequestMapping("/bridge")
public class UserBridgeController {

    @Resource
    private UserBridgeService userBridgeService;

    @PostMapping("/login")
    public String login(String account,String password){
        return userBridgeService.login(account,password);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserInfo userInfo){
        return userBridgeService.register(userInfo);
    }
    //gitee平台回调接口
    @GetMapping("/gitee")
    public String gitee(HttpServletRequest request){
        return userBridgeService.login3rd(request,"GITEE");
    }

}
