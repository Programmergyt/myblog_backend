package com.example.utils;

import com.example.mapper.BlogTagMapper;
import com.example.mapper.TagMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.entity.Blog;
import com.example.pojo.entity.Tag;
import com.example.pojo.vo.BlogVO;
import com.example.pojo.vo.TagVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 博客转换工具类
 * 负责Blog实体与VO之间的转换
 */
@Component
public class ConvertUtils {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private TagMapper tagMapper;

    /**
     * 将Blog实体转换为BlogVO
     * @param blog Blog实体
     * @return BlogVO对象
     */
    public BlogVO convertToBlogVO(Blog blog) {
        if (blog == null) {
            return null;
        }

        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);

        // 设置用户名
        blogVO.setUsername(userMapper.selectUserById(blog.getUserId()).getUsername());

        // 获取博客的标签ID列表
        List<Long> blogTagIds = blogTagMapper.selectTagIdsByBlogId(blog.getId());
        blogVO.setTagIds(blogTagIds != null ? blogTagIds : new ArrayList<>());

        // 获取标签详细信息并转换为TagVO
        if (blogTagIds != null && !blogTagIds.isEmpty()) {
            List<Tag> tags = tagMapper.selectTagsByIds(blogTagIds);
            List<TagVO> tagVOs = convertToTagVOs(tags);
            blogVO.setTags(tagVOs);
        } else {
            blogVO.setTags(new ArrayList<>());
        }

        return blogVO;
    }

    /**
     * 批量将Blog实体转换为BlogVO
     * @param blogs Blog实体列表
     * @return BlogVO列表
     */
    public List<BlogVO> convertToBlogVOs(List<Blog> blogs) {
        if (blogs == null || blogs.isEmpty()) {
            return new ArrayList<>();
        }

        return blogs.stream()
                .map(this::convertToBlogVO)
                .collect(Collectors.toList());
    }

    /**
     * 将Tag列表转换为TagVO列表
     * @param tags Tag列表
     * @return TagVO列表
     */
    public List<TagVO> convertToTagVOs(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }

        return tags.stream()
                .map(this::convertToTagVO)
                .collect(Collectors.toList());
    }

    /**
     * 将Tag实体转换为TagVO
     * @param tag Tag实体
     * @return TagVO对象
     */
    public TagVO convertToTagVO(Tag tag) {
        if (tag == null) {
            return null;
        }

        TagVO tagVO = new TagVO();
        BeanUtils.copyProperties(tag, tagVO);
        return tagVO;
    }
}