package com.example.demo.dto;

import com.example.demo.model.UserRole;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
    
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @NotNull(message = "Role is required")
    private UserRole role;
    
    // Optional fields
    @Size(max = 20, message = "Phone number must be at most 20 characters")
    private String phoneNumber;
    
    private String specialization; // For clinicians
    private String licenseNumber; // For clinicians
    private Integer age; // For patients
    private String gender; // For patients
    private String address;
    
    // Default constructor to set default role if not provided
    public RegisterRequest() {
        this.role = UserRole.CLINICIAN; // Default role
    }
}