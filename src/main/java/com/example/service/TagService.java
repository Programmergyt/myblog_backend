package com.example.service;

import com.example.pojo.Tag;
import jakarta.servlet.http.HttpServletRequest;


import java.util.List;


public interface TagService {
    // 添加新标签
    Tag createTag(String name, HttpServletRequest request);

    // 根据 ID 删除标签（如果没有被使用）
    void deleteTag(Long id, HttpServletRequest request);

    // 查询所有标签（用于博客发布时选择）
    List<Tag> getAllTags();
}
