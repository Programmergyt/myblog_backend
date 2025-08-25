package com.example;

import com.example.mapper.UserMapper;
import com.example.pojo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MywebBackendApplicationTests {

    @Autowired
    private UserMapper userMapper;
//
//    @Test
//    void testRegisterAndLogin() {
//        User user = new User();
//        user.setUsername("testuserMIKE");
//        user.setPassword("testpassword");
//        user.setEmail("testuser@example.com");
//        user.setAvatar("https://example.com/avatar.jpg");
//        user.setRole(0);
//        user.setCreateTime(java.time.LocalDateTime.now());
//
//        userMapper.insertUser(user);
//
//        User loggedInUser = userMapper.selectUserByUsernameAndPassword("testuserMIKE", "testpassword");
//        assertNotNull(loggedInUser, "User should be able to login with correct credentials");
//        assertEquals("testuser@example.com", loggedInUser.getEmail(), "Email should match the registered email");
//        userMapper.deleteUserById(loggedInUser.getId());
//    }
}