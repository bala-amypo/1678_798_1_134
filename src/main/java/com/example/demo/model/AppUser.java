package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String fullName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    public AppUser(String email, String password, String fullName, UserRole role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    // Manual getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    // Static builder method
    public static AppUserBuilder builder() {
        return new AppUserBuilder();
    }

    public static class AppUserBuilder {
        private String email;
        private String password;
        private String fullName;
        private UserRole role;
        
        public AppUserBuilder email(String email) {
            this.email = email;
            return this;
        }
        
        public AppUserBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public AppUserBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        
        public AppUserBuilder role(UserRole role) {
            this.role = role;
            return this;
        }
        
        public AppUser build() {
            return new AppUser(email, password, fullName, role);
        }
    }
}