package com.example.service;

import com.example.pojo.dto.BlogDTO;
import com.example.pojo.entity.Blog;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BlogService {
    void deleteBlog(Long id, HttpServletRequest request);
    Blog postBlog(BlogDTO blogDTO,HttpServletRequest request);
    void updateBlog(Long id,BlogDTO blogDTO,HttpServletRequest request);
    Blog getBlogById(Long id);
    List<Blog> getBlogs(String title, List<Long> tagIds, Long userId, String startTime, String endTime);
}
