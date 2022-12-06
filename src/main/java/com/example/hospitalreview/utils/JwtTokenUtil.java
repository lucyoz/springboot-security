package com.example.hospitalreview.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {
    public static boolean isExpired(String token, String secretKey){
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }

    public static Claims extractClaims(String token, String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public static String getUserName(String token, String key){
        return extractClaims(token, key).get("userName").toString();
    }

    public static String createToken(String userName, String key, long expireTimeMs){
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expireTimeMs))
                .signWith(SignatureAlgorithm.HS256,key)
                .compact();
    }
}
