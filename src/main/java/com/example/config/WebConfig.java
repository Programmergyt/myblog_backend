package com.example.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Value("${blog_image.upload-dir}")
    private String uploadPath;

    // 资源目录映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + uploadPath+'/';
        logger.info("配置静态资源映射: /images/** -> {}", location);
        registry.addResourceHandler("/images/**")
                .addResourceLocations(location);

    }

    // knife4j 配置
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API文档")
                        .version("1.0")
                        .description("基于SpringBoot3 + Vue3开发的博客项目")
                        .contact(new Contact().name("gyt").url("http://8.153.79.69").email("2403508140@qq.com"))
                );
    }

    // 修改knife4j分组名称、分组地址
    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("api")                     // 分组名称，可自定义
                .pathsToMatch("/api/**")          // 匹配所有 /api/ 开头的接口
                .build();
    }
}