package com.example.service.Impl;

import com.example.mapper.BlogMapper;
import com.example.mapper.BlogTagMapper;
import com.example.mapper.TagMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.dto.BlogDTO;
import com.example.pojo.entity.Blog;
import com.example.pojo.entity.BlogTag;
import com.example.pojo.vo.BlogVO;
import com.example.service.BlogService;
import com.example.utils.ConvertUtils;
import com.example.utils.MarkdownUtils;
import com.example.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    private ConvertUtils convertUtils;

    @Value("${blog_image.upload-dir}")
    private String uploadDir; //上传到本地的路径

    @Value("${blog_image.image-url}")
    private String imageUrl; // 中间路径：/images/

    @Value("${blog_image.base-url}")
    private String baseUrl; // 访问路径：http://localhost:8080，故图片实际上的访问路径为：http://localhost:8080/images/

    // 删除博客与对应图片
    @Override
    @Transactional
    public void deleteBlog(Long id) {
        Long userId = SecurityUtils.getUserId();

        Blog blog = blogMapper.selectBlogById(id);
        if (blog == null) {
            throw new RuntimeException("博客不存在");
        }

        Integer role = SecurityUtils.getUserRole();
        boolean isAdmin = role != null && role == 1;
        boolean isAuthor = blog.getUserId().equals(userId);
        if (!isAdmin && !isAuthor) {
            throw new RuntimeException("无权限删除此博客");
        }

        // 删除博客内容中的图片文件
        deleteImagesFromBlogContent(blog.getContent());

        // 删除博客标签关联
        blogTagMapper.deleteBlogTagBlogId(id);
        // 删除博客
        blogMapper.deleteBlogById(id);
    }

    /**
    发布博客，上传图片的函数在upload
     */
    @Override
    @Transactional
    public BlogVO postBlog(BlogDTO blogDTO) {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Blog blog = new Blog();
        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent());
        blog.setUserId(userId);
        blog.setCreateTime(LocalDateTime.now());
        blog.setUpdateTime(LocalDateTime.now());
        blogMapper.insertBlog(blog);

        // 插入博客标签关联
        if (blogDTO.getTagIds() != null && !blogDTO.getTagIds().isEmpty()) {
            List<BlogTag> blogTags = blogDTO.getTagIds().stream()
                    .map(tagId -> new BlogTag(blog.getId(), tagId))
                    .collect(Collectors.toList());
            blogTagMapper.insertBlogTags(blogTags);
        }

        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        blogVO.setUsername(userMapper.selectUserById(userId).getUsername());
        return blogVO;
    }

    /**
     * 更新博客，删除老的图片，存入新的图片
     */
    @Override
    @Transactional
    public void updateBlog(Long id, BlogDTO blogDTO) {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Integer role = SecurityUtils.getUserRole();
        Blog blog = blogMapper.selectBlogById(id);
        if (blog == null) {
            throw new RuntimeException("博客不存在");
        }

        boolean isAdmin = role != null && role == 1;
        boolean isAuthor = blog.getUserId().equals(userId);
        if (!isAdmin && !isAuthor) {
            throw new RuntimeException("无权限修改此博客");
        }

        // 删除旧内容中不再使用的图片
        deleteUnusedImagesOnUpdate(blog.getContent(), blogDTO.getContent());

        // 更新博客基本信息
        blogMapper.updateBlog(id, userId, blogDTO.getTitle(), blogDTO.getContent(), LocalDateTime.now());

        // 删除原有的博客标签关联
        blogTagMapper.deleteBlogTagBlogId(id);

        // 插入新的博客标签关联
        if (blogDTO.getTagIds() != null && !blogDTO.getTagIds().isEmpty()) {
            List<BlogTag> blogTags = blogDTO.getTagIds().stream()
                    .map(tagId -> new BlogTag(id, tagId))
                    .collect(Collectors.toList());
            blogTagMapper.insertBlogTags(blogTags);
        }
    }

    @Override
    public BlogVO getBlogById(Long id) {
        Blog blog = blogMapper.selectBlogById(id);
        if (blog == null) {
            throw new RuntimeException("博客不存在");
        }
        return convertUtils.convertToBlogVO(blog);
    }

    @Override
    @Transactional
    public List<BlogVO> getBlogs(String title, List<Long> tagIds, Long userId, String startTime, String endTime) {
        List<Blog> blogs = blogMapper.queryBlogs(title, tagIds, tagIds != null ? tagIds.size() : 0, userId, startTime, endTime);
        return convertUtils.convertToBlogVOs(blogs);
    }

    /**
     * 删除博客内容中的图片文件
     * @param content 博客内容
     */
    private void deleteImagesFromBlogContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return;
        }

        // 提取markdown中的所有图片URL
        List<String> imageUrls = MarkdownUtils.extractImageUrls(content);

        if (!imageUrls.isEmpty()) {
            // 删除本地图片文件
            // baseUrl + imageUrl 构成完整的URL前缀，如：http://localhost:8080/images/
            String urlPrefix = baseUrl + imageUrl;
            MarkdownUtils.deleteLocalImages(imageUrls, urlPrefix, uploadDir);
        }
    }

    /**
     * 更新博客时删除不再使用的图片
     * @param oldContent 旧的博客内容
     * @param newContent 新的博客内容
     */
    private void deleteUnusedImagesOnUpdate(String oldContent, String newContent) {
        if (oldContent == null || oldContent.trim().isEmpty()) {
            return;
        }

        // 提取旧内容中的图片
        List<String> oldImageUrls = MarkdownUtils.extractImageUrls(oldContent);

        // 提取新内容中的图片
        List<String> newImageUrls = MarkdownUtils.extractImageUrls(newContent);

        // 找出不再使用的图片（在旧内容中但不在新内容中）
        List<String> unusedImages = new ArrayList<>();
        for (String oldUrl : oldImageUrls) {
            if (!newImageUrls.contains(oldUrl)) {
                unusedImages.add(oldUrl);
            }
        }

        if (!unusedImages.isEmpty()) {
            // 删除不再使用的图片文件
            String urlPrefix = baseUrl + imageUrl;
            MarkdownUtils.deleteLocalImages(unusedImages, urlPrefix, uploadDir);
        }
    }
}