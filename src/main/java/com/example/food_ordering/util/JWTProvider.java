package com.example.food_ordering.util;

import com.example.food_ordering.service.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTProvider {

    @Value("${jwt_token}")
    private String secret;
    private final long ACCESS_TOKEN_EXPIRATION = 120 * 60 * 1000; // 15 minutes
    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 days

    public String generateToken(UserDetailsImpl userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("userId",userDetails.getUserId())
                .claim("profileImage",userDetails.user.getProfileImage())
                .claim("userName",userDetails.getUsername())
                .claim("email",userDetails.user.getEmail())
                .claim("phoneNumber",userDetails.user.getPhoneNumber())
                .claim("firstName",userDetails.user.getFirstName())
                .claim("lastName",userDetails.user.getLastName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateRefreshToken(UserDetailsImpl userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // veya userDetails.getUserId()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser()
               .setSigningKey(secret)
               .parseClaimsJws(token)
               .getBody()
               .getSubject();
    }

    public Long extractUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.get("userId").toString());
    }


    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

























