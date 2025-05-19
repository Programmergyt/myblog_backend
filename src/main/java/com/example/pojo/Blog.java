package com.example.pojo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data

public class Blog {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<Long> tagIds; // 用于接口传输时使用,由于数据库中没定义，所以mybatis也不会匹配这个
}
