package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.InvalidOperationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.AppUser;
import com.example.demo.model.UserRole;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }
        
        AppUser user = AppUser.builder()
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole() != null ? request.getRole() : UserRole.CLINICIAN)
                .phoneNumber(request.getPhoneNumber())
                .specialization(request.getSpecialization())
                .licenseNumber(request.getLicenseNumber())
                .active(true)
                .build();
        
        AppUser savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser);
        
        log.info("User registered successfully: {}", savedUser.getEmail());
        
        return AuthResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .role(savedUser.getRole())
                .token(token)
                .message("Registration successful")
                .build();
    }
    
    @Override
    public AuthResponse login(AuthRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase(),
                            request.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            AppUser user = userRepository.findByEmail(request.getEmail().toLowerCase())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));
            
            if (!user.getActive()) {
                throw new InvalidOperationException("Login", "User account is deactivated");
            }
            
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
            
            String token = jwtTokenProvider.generateToken(user);
            
            log.info("User logged in successfully: {}", user.getEmail());
            
            return AuthResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .role(user.getRole())
                    .token(token)
                    .message("Login successful")
                    .build();
                    
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for email: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
    }
    
    @Override
    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("No authenticated user found");
        }
        
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
    
    @Override
    public void logout(String token) {
        SecurityContextHolder.clearContext();
        log.info("User logged out");
    }
    
    @Override
    public AuthResponse refreshToken(String refreshToken) {
        // Implementation for token refresh
        // You'll need to implement refresh token logic
        throw new UnsupportedOperationException("Refresh token not implemented");
    }
    
    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidOperationException("Change password", "Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("Password changed for user: {}", user.getEmail());
    }
    
    @Override
    public void requestPasswordReset(String email) {
        AppUser user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        // Generate reset token and send email
        String resetToken = UUID.randomUUID().toString();
        // Save reset token to database or cache
        // Send email with reset link
        
        log.info("Password reset requested for email: {}", email);
    }
    
    @Override
    public void resetPassword(String token, String newPassword) {
        // Validate token and reset password
        // Implementation depends on token storage strategy
        throw new UnsupportedOperationException("Password reset not implemented");
    }
    
    @Override
    public AppUser updateProfile(Long userId, AppUser updates) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        if (updates.getFullName() != null) {
            user.setFullName(updates.getFullName());
        }
        if (updates.getPhoneNumber() != null) {
            user.setPhoneNumber(updates.getPhoneNumber());
        }
        if (updates.getSpecialization() != null) {
            user.setSpecialization(updates.getSpecialization());
        }
        if (updates.getLicenseNumber() != null) {
            user.setLicenseNumber(updates.getLicenseNumber());
        }
        
        AppUser updatedUser = userRepository.save(user);
        log.info("Profile updated for user: {}", updatedUser.getEmail());
        
        return updatedUser;
    }
    
    @Override
    public void deactivateAccount(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setActive(false);
        userRepository.save(user);
        
        log.info("Account deactivated for user: {}", user.getEmail());
    }
}