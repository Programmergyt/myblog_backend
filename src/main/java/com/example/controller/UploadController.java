package com.example.controller;

import com.example.pojo.Result;
import com.example.service.UploadService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @PostMapping
    public Result<Map<String, Object>> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        String url = uploadService.uploadImage(file ,request);
        // 返回图片链接（可按前端编辑器要求定制格式）
        Map<String, Object> result = new HashMap<>();
        result.put("url", url);
        result.put("success", 1); // Editor.md 等编辑器需要
        return Result.success(result);
    }
}
