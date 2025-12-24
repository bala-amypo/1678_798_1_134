package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Authentication", description = "Authentication and user registration endpoints")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", 
               description = "Creates a new user account with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "User registration details", required = true)
            @Valid @RequestBody RegisterRequest request) {
        
        log.info("Registering new user with email: {}", request.getEmail());
        
        AuthResponse response = authService.register(request);
        
        log.info("User registered successfully: {}", request.getEmail());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Login user", 
               description = "Authenticates user and returns JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "User credentials", required = true)
            @Valid @RequestBody AuthRequest request) {
        
        log.info("Login attempt for email: {}", request.getEmail());
        
        AuthResponse response = authService.login(request);
        
        log.info("User logged in successfully: {}", request.getEmail());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Validate token", 
               description = "Check if the provided token is valid")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token is valid"),
        @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    })
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @Parameter(description = "JWT token in Authorization header", required = true)
            @RequestHeader("Authorization") String token) {
        
        log.info("Token validation requested");
        // Token validation is handled by JwtAuthenticationFilter
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Refresh token", 
               description = "Get a new token using refresh token (simplified)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed"),
        @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @Parameter(description = "Refresh token", required = true)
            @RequestBody String refreshToken) {
        
        log.info("Token refresh requested");
        // In a real application, implement refresh token logic
        // For now, return a simple response
        AuthResponse response = AuthResponse.builder()
                .token("new-jwt-token-from-refresh")
                .message("Token refresh endpoint - implement refresh logic")
                .build();
        
        return ResponseEntity.ok(response);
    }
}