package com.example.service.Impl;

import com.example.exception.ForbiddenException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.BlogMapper;
import com.example.mapper.BlogTagMapper;
import com.example.mapper.TagMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.Blog;
import com.example.pojo.BlogTag;
import com.example.pojo.Tag;
import com.example.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    BlogTagMapper  blogTagMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TagMapper tagMapper;

    @Override
    @Transactional
    public void deleteBlog(Long id, HttpServletRequest request)
    {
        Long userId = (Long) request.getAttribute("userId");  // 从拦截器中获取
        Integer role = (Integer) request.getAttribute("role");
        // 如果是管理员则直接删除博客
        if (role == 1)
        {
            blogMapper.deleteBlogById(id);
        }
        else
        {
            Blog blog = blogMapper.selectBlogById(id);
            // 如果博客的作者id和当前用户id一致，则删除博客
            if (blog.getUserId().equals(userId))
            {
                blogMapper.deleteBlogById(id);
            }
            else {
                throw new ForbiddenException("无权限删除该博客");
            }
        }
    }

    @Override
    @Transactional
    public Blog postBlog(Blog blog) {
        // 判断用户是否存在
        if(userMapper.selectUserById(blog.getUserId())==null){throw new RuntimeException("用户不存在");}
        // 创建博客对象并设置基本信息
        Blog post_blog = new Blog();
        post_blog.setUserId(blog.getUserId());
        post_blog.setTitle(blog.getTitle());
        post_blog.setContent(blog.getContent());
        post_blog.setCreateTime(LocalDateTime.now());
        post_blog.setUpdateTime(LocalDateTime.now());

        // 插入博客到数据库
        blogMapper.insertBlog(post_blog);
        Long blogId = post_blog.getId();

        // 构造并批量插入 blog_tags 中间表
        List<BlogTag> blogTags = new ArrayList<>();
        for (Long tagId : blog.getTagIds()) {
            // 判断标签是否存在
            if(tagMapper.selectTagById(blog.getUserId())==null) {throw new RuntimeException("标签不存在");}
            blogTags.add(new BlogTag(blogId, tagId));
        }
        if (!blogTags.isEmpty()) {
            blogTagMapper.insertBlogTags(blogTags);
        }

        // 返回blog，便于controller返回给前端
        return post_blog;
    }

    @Override
    @Transactional
    public void updateBlog(Long id,Blog blog) {
        blogMapper.updateBlog(id,blog.getUserId(),blog.getTitle(), blog.getContent(), LocalDateTime.now());
        blogTagMapper.deleteBlogTagBlogId(id);
        blogTagMapper.insertBlogTags(blog.getTagIds().stream()
                .map(tagId -> new BlogTag(id, tagId))
                .toList());
    }

    @Override
    public Blog getBlogById(Long id) {
        Blog blog = blogMapper.selectBlogById(id);
        if(blog==null){
            throw new ResourceNotFoundException("博客不存在");
        }
        List<Tag> tagList = tagMapper.selectTagsByIds(blogTagMapper.selectTagIdsByBlogId(id));
        List<Long> tagIds = tagList.stream()
                .map(Tag::getId)
                .toList();
        blog.setTagIds(tagIds);
        return blog;
    }

    @Override
    public List<Blog> getBlogs(String title, Long tagId, Long userId, String startTime, String endTime) {
        List<Blog> blogList = blogMapper.queryBlogs(title, tagId, userId, startTime, endTime);
        for (Blog blog : blogList) {
            List<Tag> tagList = tagMapper.selectTagsByIds(blogTagMapper.selectTagIdsByBlogId(blog.getId()));
            List<Long> tagIds = tagList.stream()
                    .map(Tag::getId)
                    .toList();
            blog.setTagIds(tagIds);
        }
        return blogList;
    }

}
