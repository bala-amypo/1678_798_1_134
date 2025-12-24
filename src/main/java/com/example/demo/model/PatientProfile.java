package com.example.demo.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_profiles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PatientProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String patientId;
    
    @Column(nullable = false)
    private String fullName;
    
    private Integer age;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String surgeryType;
    
    @Builder.Default
    private Boolean active = true;
    
    private LocalDate surgeryDate;
    
    @CreatedDate
    private LocalDateTime createdAt;
}