package com.example.mapper;

import com.example.pojo.entity.BlogTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogTagMapper {
    int insertBlogTag(BlogTag blogTag);
    int insertBlogTags(List<BlogTag> blogTags);
    int deleteBlogTagBlogId(Long blogId);
    int deleteBlogTagByTagId(Long TagId);
    List<Long> selectTagIdsByBlogId(Long blogId);
    List<Long> selectBlogIdsByTagId(Long tagId);
}
