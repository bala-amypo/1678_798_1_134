package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs for user registration, login, and status checking")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(
        summary = "Register a new user", 
        description = "Register a new user with email, password, and full name. Returns JWT token upon successful registration."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(
        summary = "Login user", 
        description = "Authenticate user with email and password. Returns JWT token upon successful authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User authenticated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(
        summary = "Health check", 
        description = "Check if the authentication service is running and accessible"
    )
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Authentication Service");
        response.put("timestamp", java.time.Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    @Operation(
        summary = "Get service status", 
        description = "Get detailed status information about the authentication service"
    )
    @ApiResponse(responseCode = "200", description = "Service status retrieved successfully")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "Authentication API");
        status.put("version", "1.0.0");
        status.put("status", "ACTIVE");
        status.put("uptime", System.currentTimeMillis());
        status.put("endpoints", new String[]{"/register", "/login", "/health", "/status"});
        return ResponseEntity.ok(status);
    }

    @GetMapping("/info")
    @Operation(
        summary = "Get API information", 
        description = "Get information about available authentication endpoints and their usage"
    )
    @ApiResponse(responseCode = "200", description = "API information retrieved successfully")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("apiName", "Patient Recovery Tracking - Authentication API");
        info.put("description", "Handles user registration, authentication, and JWT token management");
        info.put("supportedOperations", new String[]{"User Registration", "User Login", "Health Check"});
        info.put("authenticationMethod", "JWT Bearer Token");
        info.put("contact", "demo@example.com");
        return ResponseEntity.ok(info);
    }
}