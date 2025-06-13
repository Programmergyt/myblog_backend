package com.example.controller;

import com.example.pojo.entity.Tag;
import com.example.pojo.entity.Result;
import com.example.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "博客标签接口")
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public Result<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return Result.success(tags);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(@PathVariable Long id, HttpServletRequest request) {
        tagService.deleteTag(id,request);
        return Result.success();
    }

    // TagDTO: name
    // TagVO: 与tag一致
    @PostMapping
    public Result<Tag> createTag(@RequestBody Tag tag, HttpServletRequest request) {
        Tag created_Article_tag = tagService.createTag(tag.getName(),request);
        return Result.success(created_Article_tag);
    }

}
