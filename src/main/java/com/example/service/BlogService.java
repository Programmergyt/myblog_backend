package com.example.service;

import com.example.pojo.dto.BlogDTO;
import com.example.pojo.vo.BlogVO;
import java.util.List;

public interface BlogService {
    void deleteBlog(Long id);
    BlogVO postBlog(BlogDTO blogDTO);
    void updateBlog(Long id, BlogDTO blogDTO);
    BlogVO getBlogById(Long id);
    List<BlogVO> getBlogs(String title, List<Long> tagIds, Long userId, String startTime, String endTime);
}
