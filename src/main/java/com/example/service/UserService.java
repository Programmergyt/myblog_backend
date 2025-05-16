package com.example.service;

import com.example.pojo.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User register(User user);
    User login(User user);
    void logout();
}