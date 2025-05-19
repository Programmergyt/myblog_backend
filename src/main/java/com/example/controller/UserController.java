package com.example.controller;

import com.example.pojo.Result;
import com.example.pojo.User;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // 前端可以只传过来部分属性，其他属性默认为空
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        User register_user = userService.register(user);
        return Result.success(register_user);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody User user) {
        User login_user = userService.login(user);
        String token = JwtUtil.generateToken(login_user.getId().toString(), login_user.getRole());
        return Result.success(token);
    }

    @PostMapping("/logout")
    public Result<Void> logout()
    {
        userService.logout();
        return Result.success();
    }

    @GetMapping("/me")
    public Result<User> me(HttpServletRequest request) {
        return Result.success(userService.getCurrentUser(request));
    }
}