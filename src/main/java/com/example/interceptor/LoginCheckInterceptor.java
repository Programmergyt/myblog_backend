package com.example.interceptor;

import com.example.pojo.entity.Result;
import com.example.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request,  HttpServletResponse response, Object handler) throws Exception {

        //由于URL重复，特在此放行一部分接口
        String method = request.getMethod();
        String uri = request.getRequestURI();
        // 放行查询博客和标签
        if ("GET".equalsIgnoreCase(method) && uri.matches("^/api/blogs(/\\d+)?$")) {return true;}
        if ("GET".equalsIgnoreCase(method) && uri.matches("^/api/tags")) {return true;}

		// 验证是否是登录状态
        String token = request.getHeader("Authorization");
		if (token == null || !token.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			String json = mapper.writeValueAsString(Result.error(401,"未登录"));
			try (PrintWriter out = response.getWriter()) {
				out.write(json);
			}
			return false;
		}

		// 验证令牌是否有效
		token = token.substring(7); // 去掉 "Bearer "
		if (!JwtUtil.validateToken(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			String json = mapper.writeValueAsString(Result.error(401,"令牌无效或已过期"));
			try (PrintWriter out = response.getWriter()) {
				out.write(json);
			}
			return false;
		}

        // headers里面是JWT的认证令牌，attributes里面是userid和role，parameters里面是请求参数
        Claims claims = JwtUtil.parseToken(token);
        request.setAttribute("userId", Long.parseLong(claims.getSubject()));//把userid转换为Long
        request.setAttribute("role", claims.get("role", Integer.class));//把role转换为Integer
        // 打印headers
        System.out.println("Request Headers:");
        request.getHeaderNames().asIterator().forEachRemaining(header -> {
            System.out.println(header + ": " + request.getHeader(header));
        });
        // 打印attributes
        System.out.println("userId");
        System.out.println(request.getAttribute("userId"));
        System.out.println("role");
        System.out.println(request.getAttribute("role"));
        // 打印parameters
        System.out.println("Request Parameters:");
        request.getParameterMap().forEach((key, value) -> {
            System.out.println(key + ": " + String.join(", ", value));
        });
        return true;
    }
}
