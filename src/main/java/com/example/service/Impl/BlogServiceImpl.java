package com.example.service.Impl;

import com.example.exception.ForbiddenException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.BlogMapper;
import com.example.mapper.BlogTagMapper;
import com.example.mapper.TagMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.dto.BlogDTO;
import com.example.pojo.entity.Tag;
import com.example.pojo.entity.Blog;
import com.example.pojo.entity.BlogTag;
import com.example.service.BlogService;
import com.example.utils.MarkdownUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    BlogTagMapper  blogTagMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TagMapper tagMapper;

    @Value("${blog_image.upload-dir}")
    private String uploadDir; //上传到本地的路径

    @Value("${blog_image.image-url}")
    private String imageUrl; // 中间路径：/images/

    @Value("${blog_image.base-url}")
    private String baseUrl; // 访问路径：http://localhost:8080，故图片实际上的访问路径为：http://localhost:8080/images/


    @Override
    @Transactional
    public void deleteBlog(Long id, HttpServletRequest request)
    {
        Long userId = (Long) request.getAttribute("userId");  // 从拦截器中获取
        Integer role = (Integer) request.getAttribute("role");
        Blog blog = blogMapper.selectBlogById(id);
        // 判断博客是否存在
        if (blog == null) {
            throw new ResourceNotFoundException("博客不存在");
        }

        // 管理员或作者本人可以删除
        boolean canDelete = role == 1 || blog.getUserId().equals(userId);
        if (!canDelete) {
            throw new ForbiddenException("无权限删除该博客");
        }

        // 删除博客中的图片
        List<String> imageUrls = MarkdownUtils.extractImageUrls(blog.getContent());
        MarkdownUtils.deleteLocalImages(imageUrls, baseUrl + imageUrl, uploadDir);

        // 删除博客和关联标签
        blogMapper.deleteBlogById(id);
        blogTagMapper.deleteBlogTagBlogId(id);
    }

    @Override
    @Transactional
    public Blog postBlog(BlogDTO blogDTO,HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        // 判断用户是否存在
        if(userMapper.selectUserById(userId)==null){throw new RuntimeException("用户不存在");}
        // 创建博客对象并设置基本信息
        Blog post_blog = new Blog();
        post_blog.setUserId(userId);
        post_blog.setTitle(blogDTO.getTitle());
        post_blog.setContent(blogDTO.getContent());
        post_blog.setCreateTime(LocalDateTime.now());
        post_blog.setUpdateTime(LocalDateTime.now());

        // 插入博客到数据库
        blogMapper.insertBlog(post_blog);
        Long blogId = post_blog.getId();

        // 构造并批量插入 blog_tags 中间表
        List<BlogTag> blogTags = new ArrayList<>();
        for (Long tagId : blogDTO.getTagIds()) {
            // 判断标签是否存在
            if(tagMapper.selectTagById(tagId)==null) {throw new RuntimeException("标签不存在");}
            blogTags.add(new BlogTag(blogId, tagId));
        }
        if (!blogTags.isEmpty()) {
            blogTagMapper.insertBlogTags(blogTags);
        }

        // 返回blog，便于controller返回给前端
        return post_blog;
    }

    @Override
    @Transactional
    public void updateBlog(Long id,BlogDTO blogDTO,HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        blogMapper.updateBlog(id,userId,blogDTO.getTitle(), blogDTO.getContent(), LocalDateTime.now());
        blogTagMapper.deleteBlogTagBlogId(id);
        blogTagMapper.insertBlogTags(blogDTO.getTagIds().stream()
                .map(tagId -> new BlogTag(id, tagId))
                .toList());
    }

    @Override
    public Blog getBlogById(Long id) {
        Blog blog = blogMapper.selectBlogById(id);
        if(blog==null){
            throw new ResourceNotFoundException("博客不存在");
        }
        List<Tag> tagList = tagMapper.selectTagsByIds(blogTagMapper.selectTagIdsByBlogId(id));
        List<Long> tagIds = tagList.stream()
                .map(Tag::getId)
                .toList();
        blog.setTagIds(tagIds);
        return blog;
    }

    @Override
    @Transactional
    public List<Blog> getBlogs(String title, List<Long> tagIds, Long userId, String startTime, String endTime) {
        int tagCount = (tagIds != null) ? tagIds.size() : 0;
        List<Blog> blogList = blogMapper.queryBlogs(title, tagIds, tagCount, userId, startTime, endTime);
        for (Blog blog : blogList) {
            List<Tag> tagList = tagMapper.selectTagsByIds(blogTagMapper.selectTagIdsByBlogId(blog.getId()));
            List<Long> tag_ids = tagList.stream()
                    .map(Tag::getId)
                    .toList();
            blog.setTagIds(tag_ids);
        }
        return blogList;
    }

}
