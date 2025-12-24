package com.example.demo.security;

import com.example.demo.model.AppUser;
import com.example.demo.model.UserRole;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secret;
    private final long validityInMs;

    // REQUIRED by test suite
    public JwtTokenProvider(String secret, long validityInMs) {
        this.secret = secret;
        this.validityInMs = validityInMs;
    }

    public String generateToken(AppUser user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        return ((Number) getAllClaims(token).get("userId")).longValue();
    }

    public String getEmailFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    public UserRole getRoleFromToken(String token) {
        return UserRole.valueOf((String) getAllClaims(token).get("role"));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
