package com.example.controller;

import com.example.pojo.Result;
import com.example.pojo.Stats;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/stats")
public class StatsController {
    @GetMapping
    public Result<Stats> getStats() {
        Stats stats = new Stats(100, 200, 300, 400, 500);
        return Result.success(stats);
    }
}

