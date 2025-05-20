package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    public static final int SUCCESS = 1;
    public static final int FAIL = 0;

    private Integer code;
    private String msg;
    private T data;

    // 通用成功（无数据）
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS, "【success】", null);
    }

    // 成功 + 携带数据
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS, "【success】", data);
    }

    // 失败 + 消息
    public static <T> Result<T> error(String msg) {
        return new Result<>(FAIL, "【"+msg+"】", null);
    }

    // 失败 + 自定义code
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, "【"+msg+"】", null);
    }
}
