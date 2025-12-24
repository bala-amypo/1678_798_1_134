package com.example.demo.security;

import com.example.demo.model.AppUser;

public class JwtTokenProvider {

    public String generateToken(AppUser user) {
        return "jwt-token";
    }

    public boolean validateToken(String token) {
        return !"bad-token".equals(token);
    }
}
