package com.example.service.Impl;

import com.example.exception.ForbiddenException;
import com.example.mapper.BlogTagMapper;
import com.example.mapper.TagMapper;
import com.example.pojo.entity.Tag;
import com.example.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Override
    public Tag createTag(String name, HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        // 如果是管理员则直接删除博客
        if (role == 1)
        {
            Tag tag = new Tag();
            tag.setName(name);
            tagMapper.insertTag(tag);
            return tag;
        }
        else
        {
            throw new ForbiddenException("无权限删除标签");
        }

    }

    @Override
    public void deleteTag(Long id, HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        // 如果是管理员则直接删除博客
        if (role == 1)
        {
            // 标签和博客没有关联则删除标签
            if(blogTagMapper.selectBlogIdsByTagId(id).isEmpty())
            {
                blogTagMapper.deleteBlogTagByTagId(id);
                tagMapper.deleteTagById(id);
            }
            else
            {
                throw new ForbiddenException("该标签下有博客，请先删除博客");
            }
        }
        else
        {
            throw new ForbiddenException("普通用户无权限删除标签");
        }
    }



    @Override
    public List<Tag> getAllTags() {
        return tagMapper.getAllTags();
    }
}
