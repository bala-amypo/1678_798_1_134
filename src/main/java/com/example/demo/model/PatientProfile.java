package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patient_profiles")
public class PatientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String patientId;

    private String fullName;
    private String email;
    private String surgeryType;
    private LocalDate surgeryDate;
    private Integer age;
    private String gender;
    private Boolean active;

    public PatientProfile() {}

    /* ========= BUILDER ========= */

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PatientProfile profile = new PatientProfile();

        public Builder patientId(String patientId) {
            profile.setPatientId(patientId);
            return this;
        }

        public Builder fullName(String fullName) {
            profile.setFullName(fullName);
            return this;
        }

        public Builder email(String email) {
            profile.setEmail(email);
            return this;
        }

        public Builder surgeryType(String surgeryType) {
            profile.setSurgeryType(surgeryType);
            return this;
        }

        public Builder surgeryDate(LocalDate surgeryDate) {
            profile.setSurgeryDate(surgeryDate);
            return this;
        }

        public Builder age(Integer age) {
            profile.setAge(age);
            return this;
        }

        public Builder gender(String gender) {
            profile.setGender(gender);
            return this;
        }

        public Builder active(Boolean active) {
            profile.setActive(active);
            return this;
        }

        public PatientProfile build() {
            return profile;
        }
    }

    /* ========= GETTERS ========= */

    public Long getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getSurgeryType() {
        return surgeryType;
    }

    public LocalDate getSurgeryDate() {
        return surgeryDate;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public Boolean getActive() {
        return active;
    }

    /* ========= SETTERS ========= */

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }

    public void setSurgeryDate(LocalDate surgeryDate) {
        this.surgeryDate = surgeryDate;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
