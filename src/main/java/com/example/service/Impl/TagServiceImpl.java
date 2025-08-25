package com.example.service.Impl;

import com.example.exception.ForbiddenException;
import com.example.mapper.BlogTagMapper;
import com.example.mapper.TagMapper;
import com.example.pojo.entity.Tag;
import com.example.pojo.vo.TagVO;
import com.example.service.TagService;
import com.example.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Override
    public TagVO createTag(String name) {
        Integer role = SecurityUtils.getUserRole();
        boolean isAdmin = role != null && role == 1;
        if (!isAdmin) {
            throw new RuntimeException("无权限创建标签");
        }
        Tag tag = new Tag();
        tag.setName(name);
        tagMapper.insertTag(tag);
        
        TagVO tagVO = new TagVO();
        BeanUtils.copyProperties(tag, tagVO);
        return tagVO;
    }

    @Override
    public void deleteTag(Long id) {
        Integer role = SecurityUtils.getUserRole();
        boolean isAdmin = role != null && role == 1;
        if (!isAdmin) {
            throw new RuntimeException("无权限删除标签");
        }
        
        // 检查标签是否被博客使用
        List<Long> blogIds = blogTagMapper.selectBlogIdsByTagId(id);
        if (blogIds != null && !blogIds.isEmpty()) {
            throw new RuntimeException("该标签已被博客使用，无法删除");
        }
        
        // 删除标签
        blogTagMapper.deleteBlogTagByTagId(id);
        tagMapper.deleteTagById(id);
    }

    @Override
    public List<TagVO> getAllTags() {
        List<Tag> tags = tagMapper.getAllTags();
        return tags.stream()
                .map(tag -> {
                    TagVO tagVO = new TagVO();
                    BeanUtils.copyProperties(tag, tagVO);
                    return tagVO;
                })
                .collect(Collectors.toList());
    }
}
