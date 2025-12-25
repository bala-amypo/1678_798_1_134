package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class DailySymptomLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long patientId;
    private LocalDate logDate;
    private Integer painLevel;
    private Integer mobilityLevel;
    private Integer fatigueLevel;
    private String additionalNotes;

    public DailySymptomLog() {
    }

    public DailySymptomLog(Long id, Long patientId, LocalDate logDate, Integer painLevel, Integer mobilityLevel, Integer fatigueLevel, String additionalNotes) {
        this.id = id;
        this.patientId = patientId;
        this.logDate = logDate;
        this.painLevel = painLevel;
        this.mobilityLevel = mobilityLevel;
        this.fatigueLevel = fatigueLevel;
        this.additionalNotes = additionalNotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public Integer getPainLevel() {
        return painLevel;
    }

    public void setPainLevel(Integer painLevel) {
        this.painLevel = painLevel;
    }

    public Integer getMobilityLevel() {
        return mobilityLevel;
    }

    public void setMobilityLevel(Integer mobilityLevel) {
        this.mobilityLevel = mobilityLevel;
    }

    public Integer getFatigueLevel() {
        return fatigueLevel;
    }

    public void setFatigueLevel(Integer fatigueLevel) {
        this.fatigueLevel = fatigueLevel;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailySymptomLog that = (DailySymptomLog) o;
        return Objects.equals(id, that.id) && Objects.equals(patientId, that.patientId) && Objects.equals(logDate, that.logDate) && Objects.equals(painLevel, that.painLevel) && Objects.equals(mobilityLevel, that.mobilityLevel) && Objects.equals(fatigueLevel, that.fatigueLevel) && Objects.equals(additionalNotes, that.additionalNotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, logDate, painLevel, mobilityLevel, fatigueLevel, additionalNotes);
    }

    @Override
    public String toString() {
        return "DailySymptomLog{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", logDate=" + logDate +
                ", painLevel=" + painLevel +
                ", mobilityLevel=" + mobilityLevel +
                ", fatigueLevel=" + fatigueLevel +
                ", additionalNotes='" + additionalNotes + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long patientId;
        private LocalDate logDate;
        private Integer painLevel;
        private Integer mobilityLevel;
        private Integer fatigueLevel;
        private String additionalNotes;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder patientId(Long patientId) {
            this.patientId = patientId;
            return this;
        }

        public Builder logDate(LocalDate logDate) {
            this.logDate = logDate;
            return this;
        }

        public Builder painLevel(Integer painLevel) {
            this.painLevel = painLevel;
            return this;
        }

        public Builder mobilityLevel(Integer mobilityLevel) {
            this.mobilityLevel = mobilityLevel;
            return this;
        }

        public Builder fatigueLevel(Integer fatigueLevel) {
            this.fatigueLevel = fatigueLevel;
            return this;
        }

        public Builder additionalNotes(String additionalNotes) {
            this.additionalNotes = additionalNotes;
            return this;
        }

        public DailySymptomLog build() {
            return new DailySymptomLog(id, patientId, logDate, painLevel, mobilityLevel, fatigueLevel, additionalNotes);
        }
    }
}