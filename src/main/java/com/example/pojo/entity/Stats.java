package com.example.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stats {
    private int totalPosts;
    private int totalWords;
    private int uniqueVisitors;
    private int totalViews;
    private long uptimeSeconds;
}
