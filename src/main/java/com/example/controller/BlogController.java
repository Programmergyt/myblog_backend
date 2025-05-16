package com.example.controller;

import com.example.pojo.Blog;
import com.example.pojo.Result;
import com.example.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @DeleteMapping("/{id}")
    public Result<Void> deleteBlog(@PathVariable Long id, HttpServletRequest request) {
        blogService.deleteBlog(id, request);
        return Result.success();
    }

    @PostMapping
    public Result<Blog> postBlog(@RequestBody  Blog blog) {
        Blog output_blog = blogService.postBlog(blog);
        return Result.success(output_blog);
    }

    @PutMapping("/{id}")
    public Result<Void> updateBlog(@PathVariable Long id, @RequestBody Blog blog) {
        blogService.updateBlog(id, blog);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Blog> getBlogById(@PathVariable Long id) {
        Blog blog = blogService.getBlogById(id);
        return Result.success(blog);
    }

    @GetMapping
    public Result<List<Blog>> getBlogs(@RequestParam(required = false) String title,
                                       @RequestParam(required = false) Long tagId,
                                       @RequestParam(required = false) Long userId,
                                       @RequestParam(required = false) String startTime,
                                       @RequestParam(required = false) String endTime) {
        List<Blog> blogs = blogService.getBlogs(title, tagId, userId, startTime, endTime);
        return Result.success(blogs);
    }



}
