package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.AppUser;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
    AppUser getCurrentUser();
    void logout(String token);
    AuthResponse refreshToken(String refreshToken);
    void changePassword(Long userId, String currentPassword, String newPassword);
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
    AppUser updateProfile(Long userId, AppUser updates);
    void deactivateAccount(Long userId);
}