package com.cs.crm.settings.service;

import com.cs.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    public User queryUserByLoginActAndPwd(Map<String,String> userMap);
    public List<User> getAllUser();
}
