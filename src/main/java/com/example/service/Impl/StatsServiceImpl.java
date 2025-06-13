package com.example.service.Impl;

import com.example.mapper.BlogMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.entity.Stats;
import com.example.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Stats getStats() {
        int totalPosts = blogMapper.countTotalPosts();
        int totalWords = blogMapper.countTotalWords();
        int uniqueVisitors = 0;  // 需要 Redis 实现，先设为0
        int totalViews = 0;      // 需要新增字段或 Redis，先设为0

        LocalDateTime earliestCreateTime = userMapper.getEarliestCreateTime();
        long uptimeSeconds = Duration.between(earliestCreateTime, LocalDateTime.now()).getSeconds();

        return new Stats(totalPosts, totalWords, uniqueVisitors, totalViews, uptimeSeconds);
    }
}
