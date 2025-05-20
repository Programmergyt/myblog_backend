package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mapper")  // 让 Spring 扫描 `mapper` 包中的所有 Mapper 接口
public class MywebBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MywebBackendApplication.class, args);
    }

}
