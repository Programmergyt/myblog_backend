package com.example.filter;

import com.example.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * JWT认证过滤器的主要处理逻辑
     * 1. 从请求头中获取JWT令牌
     * 2. 验证令牌的有效性
     * 3. 解析令牌中的用户信息
     * 4. 将用户信息保存到SecurityContext中
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        // 从请求头中获取Authorization字段
        String authHeader = request.getHeader("Authorization");
        
        // 如果请求头中没有Authorization字段或不是Bearer token，则直接放行
        // 此时创建一个 AnonymousAuthenticationToken
        // 给它附加默认权限：ROLE_ANONYMOUS
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 提取JWT令牌（去掉"Bearer "前缀）
        String jwt = authHeader.substring(7);
        
        // 验证JWT令牌的有效性
        if (!JwtUtil.validateToken(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 解析JWT令牌，获取用户信息
        Claims claims = JwtUtil.parseToken(jwt);
        Long userId = Long.parseLong(claims.getSubject());
        Integer role = claims.get("role", Integer.class);

        // 创建认证令牌，包含用户ID和角色信息
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userId,  // 用户ID作为principal
            null,    // credentials为null，因为JWT已经包含了认证信息
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))  // 用户角色
        );

        // 将认证信息保存到SecurityContext中
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        // 继续处理请求
        filterChain.doFilter(request, response);
    }
} 