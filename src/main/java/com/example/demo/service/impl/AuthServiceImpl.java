package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.AppUser;
import com.example.demo.model.UserRole;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthServiceImpl implements AuthService {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtTokenProvider jwt;

    public AuthServiceImpl(
            AppUserRepository repo,
            PasswordEncoder encoder,
            AuthenticationManager manager,
            JwtTokenProvider jwt
    ) {
        this.repo = repo;
        this.encoder = encoder;
        this.manager = manager;
        this.jwt = jwt;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(UserRole.CLINICIAN);

        repo.save(user);
        return new AuthResponse(user.getEmail(), jwt.generateToken(user));
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        AppUser user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new AuthResponse(user.getEmail(), jwt.generateToken(user));
    }
}
