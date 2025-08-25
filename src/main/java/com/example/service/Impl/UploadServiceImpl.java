package com.example.service.Impl;

import com.example.service.UploadService;
import com.example.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService{

    private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    //  uploadDir 是配置文件或代码中注入的路径
    @Value("${blog_image.upload-dir}")
    private String uploadDir;

    @Value("${blog_image.image-url}")
    private String imageBaseUrl;

    @Value("${blog_image.base-url}")
    private String baseUrl;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        // 原始文件名和后缀
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + extension;
        logger.info("生成的文件名: {}", fileName);

        // 创建保存目录（如不存在）
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean output = dir.mkdirs();
            logger.info("创建目录: {}, 结果: {}", dir.getAbsolutePath(), output);
        }

        // 保存文件到本地指定路径
        File dest = new File(dir, fileName);
        file.transferTo(dest);
        logger.info("文件保存路径: {}", dest.getAbsolutePath());

        // 生成访问URL
        String url = baseUrl + imageBaseUrl + fileName;
        logger.info("生成的图片URL: {}", url);
        
        return url;
    }
}
