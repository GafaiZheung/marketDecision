package com.example.marketdecision.service;

import com.example.marketdecision.dao.LoginDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginService
{
    @Resource
    private LoginDao loginDao;

    public String check(String userID)
    {
        return loginDao.check(userID);
    }
}
