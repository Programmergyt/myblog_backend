package com.example.service;

import com.example.pojo.dto.LoginDTO;
import com.example.pojo.dto.RegisterDTO;
import com.example.pojo.vo.UserVO;

public interface UserService {
    UserVO register(RegisterDTO registerDTO);
    String login(LoginDTO loginDTO);
    void logout();
    UserVO getCurrentUser();
}