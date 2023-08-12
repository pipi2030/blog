package com.neutech.mapper;

import com.neutech.entity.User;
import com.neutech.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserService userService;
    @Test
    public void getPassByUserName(){
        User user = userService.getPassByUserName("徐阳");
    }
    @Test
    public void Test(){
        User user = userService.getUserByUserName("徐阳");
        System.err.println(user);
    }
}
