package com.example.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsVO implements Serializable {
    private int totalPosts;
    private int totalWords;
    private int uniqueVisitors;
    private int totalViews;
    private long uptimeSeconds;
}
