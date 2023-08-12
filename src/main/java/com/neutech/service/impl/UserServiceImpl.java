package com.neutech.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neutech.entity.User;
import com.neutech.mapper.UserMapper;
import com.neutech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getUserByUserId(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User getPassByUserName(String userName) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",userName);
        return userMapper.selectOne(wrapper);
    }
    @Override
    public User getUserByUserName(String userName){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",userName);
        return userMapper.selectOne(wrapper);
    };
}
