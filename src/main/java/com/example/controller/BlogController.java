package com.example.controller;

import com.example.pojo.dto.BlogDTO;
import com.example.pojo.entity.Result;
import com.example.pojo.vo.BlogVO;
import com.example.service.BlogService;
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
    public Result<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return Result.success();
    }

    @PostMapping
    public Result<BlogVO> postBlog(@RequestBody BlogDTO blogDTO) {
        return Result.success(blogService.postBlog(blogDTO));
    }

    @PutMapping("/{id}")
    // BlogDTO: title,content,tagIds
    public Result<Void> updateBlog(@PathVariable Long id, @RequestBody BlogDTO blogDTO) {
        blogService.updateBlog(id, blogDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    // BlogVO: userId,[username],title,content,tagIds,[tags],createTime,updateTime
    public Result<BlogVO> getBlogById(@PathVariable Long id) {
        return Result.success(blogService.getBlogById(id));
    }

    @GetMapping
    public Result<List<BlogVO>> getBlogs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.success(blogService.getBlogs(title, tagIds, userId, startTime, endTime));
    }
}
