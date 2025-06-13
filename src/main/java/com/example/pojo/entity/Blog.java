package com.example.pojo.entity;

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
    private List<Long> tagIds; // 准备干掉
}
