package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_profiles",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "patient_id"),
           @UniqueConstraint(columnNames = "email")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "patient_id", nullable = false, length = 20, unique = true)
    private String patientId;
    
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    
    @Column(nullable = false)
    private Integer age;
    
    @Column(length = 10)
    private String gender;
    
    @Column(length = 100)
    private String email;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "surgery_type", nullable = false, length = 50)
    private String surgeryType; // KNEE, HIP, SHOULDER, SPINE, etc.
    
    @Column(name = "surgery_date", nullable = false)
    private LocalDateTime surgeryDate;
    
    @Column(name = "attending_clinician_id")
    private Long attendingClinicianId;
    
    @Column(length = 500)
    private String medicalHistory;
    
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;
    
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;
    
    @Column(length = 500)
    private String address;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = true;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper method to get days since surgery
    public Long getDaysSinceSurgery() {
        return java.time.temporal.ChronoUnit.DAYS.between(
            surgeryDate.toLocalDate(), 
            LocalDateTime.now().toLocalDate()
        );
    }
}