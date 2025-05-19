package com.example.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.pojo.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex){
        logger.error("未处理异常", ex);
        return Result.error("其他错误："+ex.getMessage());
    }

    // 处理用户注册时用户名或邮箱已存在的异常
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<Void> handleDuplicateKeyException(DuplicateKeyException ex) {
        return Result.error("重复插入元素："+ex.getMessage());
    }

    // 处理用户登录时用户名或密码错误的异常
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        return Result.error("非法参数："+ex.getMessage());
    }

    // 处理用户操作时资源不存在的异常
    @ExceptionHandler(ResourceNotFoundException.class)
    public Result<Void> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return Result.error("资源不存在："+ex.getMessage());
    }

    // 处理上传文件大小超过限制的异常
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return Result.error("上传文件大小超过限制："+ex.getMessage());
    }
    
	// 处理用户没有权限（未登录或不是管理员）的异常
    @ExceptionHandler(ForbiddenException.class)
    public Result<Void> handleForbiddenException(ForbiddenException ex) {
        return Result.error("禁止访问："+ex.getMessage());
    }
    
	// 处理其他异常
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException ex) {
        return Result.error("运行错误: " + ex.getMessage());
    }
}