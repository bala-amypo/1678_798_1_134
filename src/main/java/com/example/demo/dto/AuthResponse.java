package com.example.demo.dto;

import com.example.demo.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private Long id;
    private String email;
    private String fullName;
    private UserRole role;
    private String token;
    
    @Builder.Default
    private String tokenType = "Bearer";
    
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private String message;
    
    // Convenience constructor for success responses
    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
        this.tokenType = "Bearer";
    }
    
    // Convenience constructor for detailed responses
    public AuthResponse(Long id, String email, String fullName, UserRole role, String token, String message) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.token = token;
        this.tokenType = "Bearer";
        this.message = message;
        this.issuedAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusHours(24);
    }
}