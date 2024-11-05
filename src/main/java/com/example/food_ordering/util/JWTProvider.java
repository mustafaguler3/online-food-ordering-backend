package com.example.food_ordering.util;

import com.example.food_ordering.service.UserDetailsImpl;
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
    @Value("${jwt_exp}")
    private int expiration;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("profileImage",userDetails.getUser().getProfileImage())
                .claim("userName",userDetails.getUsername())
                .claim("email",userDetails.getUser().getEmail())
                .claim("phoneNumber",userDetails.getUser().getPhoneNumber())
                .claim("firstName",userDetails.getUser().getFirstName())
                .claim("lastName",userDetails.getUser().getLastName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 60 * 1000))
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

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

























