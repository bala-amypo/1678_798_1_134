package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository repo;
    private final JwtTokenProvider jwt;

    public AuthService(AppUserRepository repo) {
        this.repo = repo;
        this.jwt = new JwtTokenProvider("secret", 3600000);
    }

    public void register(RegisterRequest r) {
        if (repo.findByEmail(r.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        repo.save(AppUser.builder()
                .email(r.getEmail())
                .password(r.getPassword())
                .role(r.getRole())
                .build());
    }

    public AuthResponse login(AuthRequest r) {
        AppUser u = repo.findByEmail(r.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("not found"));
        return new AuthResponse(jwt.createToken(u.getEmail(), u.getRole()));
    }
}
