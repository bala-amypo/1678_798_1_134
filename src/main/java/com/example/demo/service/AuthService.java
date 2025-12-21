package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.AppUser;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AppUser register(AppUser user);
    AuthResponse login(AuthRequest request);
    AppUser findByEmail(String email);
}