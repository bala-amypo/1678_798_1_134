package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String patientId;

    @NotBlank
    private String fullName;

    @NotNull
    @Positive
    private Integer age;

    @Column(unique = true)
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String surgeryType;

    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public PatientProfile(String patientId, String fullName, Integer age, String email, String surgeryType) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.surgeryType = surgeryType;
        this.active = true;
    }

    // Manual getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSurgeryType() { return surgeryType; }
    public void setSurgeryType(String surgeryType) { this.surgeryType = surgeryType; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}