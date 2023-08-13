package com.neutech.controller;

import com.neutech.entity.User;
import com.neutech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/getUser")
    public User getUserById(Integer userId){
        return userService.getUserByUserId(userId);
    }
    @GetMapping("/login")
    public String login(String userName, String pass,HttpSession httpSession){
        User user = userService.getPassByUserName(userName);
        if(user == null){//未注册
            return "-1";
        }
        else if(!user.getPass().equals(pass)){//密码不正确
            return "0";
        }
        else {//成功登陆
            httpSession.setAttribute("user",user);//方便未登陆拦截
            return "1";
        }
    }
    @GetMapping("/getUserAfterLogin")
    public User getUserData(String userName){
        return userService.getUserByUserName(userName);
    }
}
