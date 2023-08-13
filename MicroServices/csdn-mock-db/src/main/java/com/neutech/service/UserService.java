package com.neutech.service;

import com.neutech.entity.User;

public interface UserService {
    User getUserByUserId(Integer userId);
    User getPassByUserName(String userName);
    User getUserByUserName(String userName);
}
