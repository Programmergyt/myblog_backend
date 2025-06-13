package com.example.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownUtils {

    // 提取 markdown 中的所有图片 URL
    public static List<String> extractImageUrls(String content) {
        List<String> urls = new ArrayList<>();
        if (content == null) return urls;

        String regex = "!\\[[^\\]]*]\\((.*?)\\)";
        Matcher matcher = Pattern.compile(regex).matcher(content);
        while (matcher.find()) {
            urls.add(matcher.group(1));
        }
        return urls;
    }

    // 根据图片 URL 删除本地文件
    public static void deleteLocalImages(List<String> urls, String urlPrefix, String localPathPrefix) {
        for (String url : urls) {
            if (url.startsWith(urlPrefix)) {
                String filePath = url.replace(urlPrefix, localPathPrefix+"//");
                File file = new File(filePath);
                System.out.println("文件:" + filePath+",是否存在:"+file.exists());
                if (file.exists()) {
                    boolean result = file.delete();
                    System.out.println("删除文件：" + filePath);
                    System.out.println("结果：" + result);
                }
            }
        }
    }
}
