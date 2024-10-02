package com.example.medicine.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtTokenUtil {

//    private static final String SECRET_KEY = "your-secret-key"; // 替换为你的密钥
private static final String SECRET_KEY = "Hyh"; // 替换为你的密钥

    private static final long EXPIRATION_TIME = 86400000; // 24小时过期时间

    public static String generateToken(String userAccount) {
        return Jwts.builder()
                .setSubject(userAccount)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
