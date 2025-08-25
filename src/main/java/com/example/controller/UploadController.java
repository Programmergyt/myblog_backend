package com.example.controller;

import com.example.pojo.entity.Result;
import com.example.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "文件上传接口")
@RequestMapping("/api")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public Result<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String url = uploadService.uploadImage(file);
        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        return Result.success(data);
    }
}
