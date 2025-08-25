package com.example.service;

import com.example.pojo.vo.TagVO;
import java.util.List;

public interface TagService {
    // 添加新标签
    TagVO createTag(String name);

    // 根据 ID 删除标签（如果没有被使用）
    void deleteTag(Long id);

    // 查询所有标签（用于博客发布时选择）
    List<TagVO> getAllTags();
}
