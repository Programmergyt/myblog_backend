package com.example.service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface UploadService {
     String uploadImage(MultipartFile file, HttpServletRequest request) throws IOException;
}
