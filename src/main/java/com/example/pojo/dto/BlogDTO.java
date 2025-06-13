package com.example.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogDTO {
    private String title;
    private String content;
    private List<Long> tagIds;
}
