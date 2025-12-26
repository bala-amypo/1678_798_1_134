package com.example.demo.security;

import com.example.demo.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    
    private final JwtUtil jwtUtil;
    
    public JwtTokenProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    public String generateToken(AppUser user) {
        return jwtUtil.generateTokenForUser(user);
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            return jwtUtil.isTokenValid(token, username);
        } catch (Exception e) {
            return false;
        }
    }
}