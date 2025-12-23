package com.example.demo.service.impl;

// ======= PROJECT DTOs =======
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;

// ======= PROJECT MODELS =======
import com.example.demo.model.AppUser;
import com.example.demo.model.UserRole;

// ======= PROJECT REPOSITORIES =======
import com.example.demo.repository.AppUserRepository;

// ======= PROJECT SERVICES =======
import com.example.demo.service.AuthService;

// ======= SECURITY =======
import com.example.demo.security.JwtTokenProvider;

// ======= SPRING SECURITY =======
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

// ======= SPRING CORE =======
import org.springframework.stereotype.Service;

// ======= JAVA =======
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        Optional<AppUser> existing =
                appUserRepository.findByEmail(request.getEmail());

        if (existing.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        AppUser user = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(UserRole.CLINICIAN)
                .build();

        AppUser savedUser = appUserRepository.save(user);

        String token = jwtTokenProvider.generateToken(savedUser);

        return new AuthResponse(savedUser.getEmail(), token);
    }

    @Override
    public AuthResponse login(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        AppUser user = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid credentials"));

        String token = jwtTokenProvider.generateToken(user);

        return new AuthResponse(user.getEmail(), token);
    }
}
