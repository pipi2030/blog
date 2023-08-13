package com.neutech.controller;

import com.neutech.entity.User;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
//    @DubboReference(interfaceClass = UserService.class,interfaceName = "${es.service.name}",version = "${es.service.version}")
//    private UserService userService;
//    @GetMapping("/user/{userId}")
//    public User UserController(@PathVariable int userId){
//        return userService.getUserByUserId(userId);
//    }
}
