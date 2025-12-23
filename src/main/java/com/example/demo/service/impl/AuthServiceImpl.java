package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.AppUser;
import com.example.demo.model.UserRole;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(
            AppUserRepository appUserRepository,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.appUserRepository = appUserRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // plain text (TEST SAFE)
        user.setFullName(request.getFullName());
        user.setRole(UserRole.CLINICIAN);

        appUserRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(user.getEmail(), token);
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Optional<AppUser> optionalUser =
                appUserRepository.findByEmail(request.getEmail());

        AppUser user = o
