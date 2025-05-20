package com.example.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "my-super-secret-key-for-jwt-signing-which-should-be-long";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(
            java.util.Base64.getEncoder().encodeToString(SECRET.getBytes())));

    private static final long EXPIRATION_TIME = 3600000;

    public static String generateToken(String userId,int role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(userId)//  用户ID，注意是字符串
                .claim("role", role)// 角色（普通用户或者管理员），注意是整型
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
