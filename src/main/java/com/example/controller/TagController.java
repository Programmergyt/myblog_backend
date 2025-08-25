package com.example.controller;

import com.example.pojo.dto.TagDTO;
import com.example.pojo.entity.Result;
import com.example.pojo.vo.TagVO;
import com.example.service.TagService;
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
    public Result<List<TagVO>> getAllTags() {
        return Result.success(tagService.getAllTags());
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success();
    }

    @PostMapping
    public Result<TagVO> createTag(@RequestBody TagDTO tagDTO) {
        return Result.success(tagService.createTag(tagDTO.getName()));
    }
}
