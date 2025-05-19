package com.example.config;

import com.example.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:application.properties")
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Value("${blog_image.upload-dir}")
    private String uploadPath;
    //登录认证放拦截器，业务权限验证放 Service 层
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login", "/api/auth/register",  "/api/stats", "/api/upload");
    }
//    请求路径：http://localhost:8080/images/xxx.jpg
//    实际物理路径：D:/develop/myweb_backend_blog_imgs/xxx.jpg
//    所以前端只要发个图片过来，我存到d盘，把image开头的路径发给他，他访问image开头的路径就会映射到我的d盘
//    addResourceLocations(...) 方法要求的路径是 Spring 能识别的资源定位符
//    "file:" 是 协议前缀，告诉 Spring：你要加载的是本地磁盘文件
//    '/'是目录分隔符，确保路径拼接不会出错
//    ResourceHandler 是目录映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}