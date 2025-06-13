package com.example.controller;

import com.example.pojo.dto.BlogDTO;
import com.example.pojo.entity.Blog;
import com.example.pojo.entity.Result;
import com.example.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "博客接口")
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @DeleteMapping("/{id}")
    // 无需新定义
    public Result<Void> deleteBlog(@PathVariable Long id, HttpServletRequest request) {
        blogService.deleteBlog(id, request);
        return Result.success();
    }

    //
    @PostMapping
    // BlogDTO: title,content,tagIds
    // BlogVO: userId,[username],title,content,tagIds,[tags],createTime,updateTime
    public Result<Blog> postBlog(@RequestBody BlogDTO blogDTO, HttpServletRequest request) {
        Blog output_blog = blogService.postBlog(blogDTO,request);
        return Result.success(output_blog);
    }

    @PutMapping("/{id}")
    // BlogDTO: title,content,tagIds
    public Result<Void> updateBlog(@PathVariable Long id, @RequestBody BlogDTO blogDTO, HttpServletRequest request) {
        blogService.updateBlog(id, blogDTO,request);
        return Result.success();
    }

    @GetMapping("/{id}")
    // BlogVO: userId,[username],title,content,tagIds,[tags],createTime,updateTime
    public Result<Blog> getBlogById(@PathVariable Long id) {
        Blog blog = blogService.getBlogById(id);
        return Result.success(blog);
    }

    @GetMapping
    // BlogVO: userId,[username],title,content,tagIds,[tags],createTime,updateTime
    public Result<List<Blog>> getBlogs(@RequestParam(required = false) String title,
                                       @RequestParam(required = false) List<Long> tagIds,
                                       @RequestParam(required = false) Long userId,
                                       @RequestParam(required = false) String startTime,
                                       @RequestParam(required = false) String endTime) {
        List<Blog> blogs = blogService.getBlogs(title, tagIds, userId, startTime, endTime);
        return Result.success(blogs);
    }

}
