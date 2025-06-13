package com.example.service.Impl;

import com.example.mapper.UserMapper;
import com.example.pojo.entity.User;
import com.example.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User register(User user) {
        User register_user = new User();
        register_user.setUsername(user.getUsername());
        register_user.setPassword(user.getPassword()); // 实际应用中应加密存储
        register_user.setEmail(user.getEmail());
        register_user.setRole(0); // 默认普通用户
        register_user.setCreateTime(LocalDateTime.now());
        register_user.setAvatar(user.getAvatar());
        userMapper.insertUser(register_user);
        return register_user;
    }

    @Override
    public User login(User user) {
        User select_user = userMapper.selectUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (select_user == null) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        return select_user;
    }

    @Override
    public void logout() {
        // 实现登出逻辑，如清除会话等
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");  // 从拦截器中获取
        return userMapper.selectUserById(userId);
    }
}