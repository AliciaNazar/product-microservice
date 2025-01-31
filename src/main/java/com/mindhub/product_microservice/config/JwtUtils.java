package com.mindhub.product_microservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }


    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Long extractId(String token){
        String id = parseClaims(token).get("id",String.class);
        return Long.parseLong(id);
    }
    public String extractRole(String token){
        return parseClaims(token).get("role",String.class);
    }

    public boolean validateToken(String token) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(tokenUsername) && !isTokenExpired(token));
    }

    Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    public String tokenParser(String authorization){
        return authorization.substring(7);
    }

    public String getEmailFromToken(String authorization){
        String token = authorization.substring(7);
        return extractEmail(token);
    }
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }
}
