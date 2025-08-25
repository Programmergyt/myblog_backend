package com.example.service.Impl;

import com.example.mapper.BlogMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.vo.StatsVO;
import com.example.service.StatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserMapper userMapper;

    @Cacheable(value = "siteStats", key = "'summary'")
    @Override
    public StatsVO getStats() {
        log.info("【StatsService】执行了 getStats() 方法，缓存未命中或已过期");
        int totalPosts = blogMapper.countTotalPosts();
        int totalWords = blogMapper.countTotalWords();
        int uniqueVisitors = 0;  // 需要 Redis 实现，先设为0
        int totalViews = 0;      // 需要新增字段或 Redis，先设为0

        LocalDateTime earliestCreateTime = userMapper.getEarliestCreateTime();
        long uptimeSeconds = Duration.between(earliestCreateTime, LocalDateTime.now()).getSeconds();

        return new StatsVO(totalPosts, totalWords, uniqueVisitors, totalViews, uptimeSeconds);
    }
}
