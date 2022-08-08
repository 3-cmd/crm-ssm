package com.cs.crm.settings.service.impl;

import com.cs.crm.settings.domain.User;
import com.cs.crm.settings.mapper.UserMapper;
import com.cs.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String,String> userMap) {
        return userMapper.selectUserByLoginActAndPwd(userMap);
    }

    @Override
    public List<User> getAllUser() {
        List<User> allUser = userMapper.getAllUser();
        return allUser;
    }
}
