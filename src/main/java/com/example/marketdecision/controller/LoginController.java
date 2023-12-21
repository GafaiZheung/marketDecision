package com.example.marketdecision.controller;


import com.example.marketdecision.Bean.Status;
import com.example.marketdecision.service.LoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class LoginController
{

    @Resource
    private LoginService loginService;
    /**
     * 比对账号密码
     * 对应前端check请求
     */
    @RequestMapping(value = "check", method = RequestMethod.POST)
    public Status check(String userID, String userPassword)
    {
        if(loginService.check(userID) == null)
            return new Status(-1, "用户不存在");
        if(loginService.check(userID).equals(userPassword))
            return new Status(1, "登录成功");
        else
            return new Status(0, "密码错误");
    }
}
