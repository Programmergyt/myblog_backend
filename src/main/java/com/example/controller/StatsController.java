package com.example.controller;

import com.example.pojo.entity.Result;
import com.example.pojo.entity.Stats;
import com.example.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "网站数据接口")
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    // statsVO:与stats一致
    public Result<Stats> getStats() {
        Stats stats = statsService.getStats();
        return Result.success(stats);
    }
}

