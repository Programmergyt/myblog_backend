package com.example.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogVO implements Serializable {
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String content;
    private List<Long> tagIds;
    private List<TagVO> tags; //TagVO包括id和name
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
