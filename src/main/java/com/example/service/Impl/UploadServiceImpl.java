package com.example.service.Impl;

import com.example.service.UploadService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService{

    //  uploadDir 是配置文件或代码中注入的路径
    @Value("${blog_image.upload-dir}")
    private String uploadDir;

    @Value("${blog_image.image-base-url}")
    private String imageBaseUrl; // 例如：http://localhost:8080/images/

    public String uploadImage(MultipartFile file) throws IOException {
        // 原始文件名和后缀
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + extension;

        // 创建保存目录（如不存在）
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean output = dir.mkdirs();
            System.out.println("创建目录：" + dir.getAbsolutePath() + "，结果：" + output);
        }

        // 保存文件到指定路径
        File dest = new File(dir, fileName);
        file.transferTo(dest);

        // 返回图片访问 URL
        return imageBaseUrl + fileName;
    }
}
