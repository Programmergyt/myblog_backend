package com.example.service;

import com.example.pojo.Blog;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BlogService {
    void deleteBlog(Long id, HttpServletRequest request);
    Blog postBlog(Blog blog);
    void updateBlog(Long id,Blog blog);
    Blog getBlogById(Long id);
    List<Blog> getBlogs(String title, Long tagId, Long userId, String startTime, String endTime);
}
