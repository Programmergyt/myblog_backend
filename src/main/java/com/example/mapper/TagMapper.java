package com.example.mapper;

import com.example.pojo.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {
    int insertTag(Tag tag);
    Tag selectTagById(Long id);
    List<Tag> selectTagsByIds(List<Long> ids);
    List<Tag> getAllTags();
    int deleteTagById(Long id);
    int updateTagById(Long id);
}
