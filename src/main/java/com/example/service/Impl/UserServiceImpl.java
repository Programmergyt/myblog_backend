package com.example.service.Impl;

import com.example.mapper.UserMapper;
import com.example.pojo.dto.LoginDTO;
import com.example.pojo.dto.RegisterDTO;
import com.example.pojo.entity.User;
import com.example.pojo.vo.UserVO;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import com.example.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVO register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (userMapper.selectUserByUsername(registerDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户实体
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setRole(0); // 默认普通用户
        user.setCreateTime(LocalDateTime.now());

        // 保存用户
        userMapper.insertUser(user);

        // 转换为VO返回
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public String login(LoginDTO loginDTO) {
        // 根据用户名查询用户
        User user = userMapper.selectUserByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 验证密码
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成JWT令牌
        return JwtUtil.generateToken(user.getId().toString(), user.getRole());
    }

    @Override
    public void logout() {
        // JWT是无状态的，服务端不需要处理登出
        // 客户端需要删除存储的token
    }

    @Override
    @Cacheable(value = "userCache", key = "#root.methodName + '_' + T(com.example.utils.SecurityUtils).getUserId()")
    public UserVO getCurrentUser() {
        log.info("【UserService】执行了 getCurrentUser() 方法，缓存未命中或已过期");
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        User user = userMapper.selectUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}