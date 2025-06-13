package com.example.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class GlobalLogAspect {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Pointcut("execution(* com.example.controller..*(..))") // 所有 controller 包下方法
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("━━━━━━━━━━ 🟢 请求开始 ━━━━━━━━━━");
        log.info("➡️ 请求方式: [{}] {}", request != null ? request.getMethod() : "N/A", request != null ? request.getRequestURI() : "N/A");
        log.info("🎯 调用方法: {}", methodName);
        log.info("📦 参数: {}", Arrays.toString(args));
        log.info("🌐 IP: {}", request != null ? request.getRemoteAddr() : "unknown");

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("✅ 响应成功，耗时: {} ms", duration);

            // 🔽 分隔线 - 结束
            log.info("━━━━━━━━━━ ✅ 请求结束 ━━━━━━━━━━");
            return result;
        } catch (Throwable e) {
            long duration = System.currentTimeMillis() - start;
            log.error("❌ 请求异常，耗时: {} ms", duration, e);
            // 🔽 分隔线 - 异常结束
            log.info("━━━━━━━━━━ ❌ 请求结束 ━━━━━━━━━━");
            throw e;
        }
    }
}
