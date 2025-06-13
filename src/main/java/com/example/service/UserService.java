package com.example.service;

import com.example.pojo.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    User register(User user);
    User login(User user);
    void logout();
    User getCurrentUser(HttpServletRequest request);
}