package com.example.controller;

import com.example.pojo.entity.Result;
import com.example.pojo.entity.User;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "用户认证接口")
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // 前端可以只传过来部分属性，其他属性默认为空
    // RegisterDTO:username,password,email,avatar
    // public class UserVO {
    //    private Long id;
    //    private String username;
    //    private String email;
    //    private String avatar;
    //    private Integer role;
    //}
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        User register_user = userService.register(user);
        return Result.success(register_user);
    }

    @PostMapping("/login")
//    public class LoginDTO {
//        private String username;
//        private String password;
//    }
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

//    public class UserVO {
//        private Long id;
//        private String username;
//        private String email;
//        private String avatar;
//        private Integer role;
//    }
    @GetMapping("/me")
    public Result<User> me(HttpServletRequest request) {
        return Result.success(userService.getCurrentUser(request));
    }
}