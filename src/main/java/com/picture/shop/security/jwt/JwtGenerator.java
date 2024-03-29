package com.picture.shop.security.jwt;

import com.picture.shop.controller.exception.JwtValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
public class JwtGenerator {
    public String generateForgotPassToken(String email) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.PJWT_SECRET)
                .compact();
        return token;
    }
    public String generatePassToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.PJWT_SECRET)
                .compact();
        return token;
    }
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(email)
                .claim("authorities", authorities)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact();
        return token;
    }

    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public String getEmailFromPJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.PJWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(SecurityConstants.JWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new JwtValidationException("JWT was expired or incorrect");
        }
    }
    public boolean validatePassToken(String token){
        try {
            Jwts
                    .parser()
                    .setSigningKey(SecurityConstants.PJWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new JwtValidationException("PJWT was expired or incorrect");
        }
    }
}
