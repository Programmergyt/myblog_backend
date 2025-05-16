package com.example.mapper;

import com.example.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BlogMapper {
    // 新增博客，与新增博客和标签的关联一起完成
    int insertBlog(Blog blog);

    // 更新博客
    int updateBlog(Long id, Long userId, String title, String content, LocalDateTime updateTime);

    // 根据ID查询博客
    Blog selectBlogById(@Param("id") Long id);

    // 根据ID删除博客
    int  deleteBlogById(@Param("id") Long id);

    // 根据条件查询博客
    List<Blog> queryBlogs(@Param("title") String title,
                          @Param("tagId") Long tagId,
                          @Param("userId") Long userId,
                          @Param("startTime") String startTime,
                          @Param("endTime") String endTime);
}
