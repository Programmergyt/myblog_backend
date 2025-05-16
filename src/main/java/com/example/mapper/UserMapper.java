package com.example.mapper;

import org.apache.ibatis.annotations.*;
import com.example.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {

    // 新增用户
    int insertUser(User user);

    // 根据ID查询用户
    User selectUserById(@Param("id") Long id);

    // 根据用户名查询用户
    User selectUserByUsername(@Param("username") String username);

    // 更新用户信息
    int updateUser(User user);

    // 根据ID删除用户
    int deleteUserById(@Param("id") Long id);

    // 查询所有用户
    List<User> selectAllUsers();

    // 根据用户名和密码查询（登录验证）
    User selectUserByUsernameAndPassword(@Param("username") String username,
                                         @Param("password") String password);

    // 根据邮箱查询用户
    User selectUserByEmail(@Param("email") String email);
}