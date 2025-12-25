package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class DeviationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ruleCode;
    private String parameter;
    private Integer threshold;
    private String severity;
    private Boolean active = true;

    public DeviationRule() {
    }

    public DeviationRule(Long id, String ruleCode, String parameter, Integer threshold, String severity, Boolean active) {
        this.id = id;
        this.ruleCode = ruleCode;
        this.parameter = parameter;
        this.threshold = threshold;
        this.severity = severity;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviationRule that = (DeviationRule) o;
        return Objects.equals(id, that.id) && Objects.equals(ruleCode, that.ruleCode) && Objects.equals(parameter, that.parameter) && Objects.equals(threshold, that.threshold) && Objects.equals(severity, that.severity) && Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruleCode, parameter, threshold, severity, active);
    }

    @Override
    public String toString() {
        return "DeviationRule{" +
                "id=" + id +
                ", ruleCode='" + ruleCode + '\'' +
                ", parameter='" + parameter + '\'' +
                ", threshold=" + threshold +
                ", severity='" + severity + '\'' +
                ", active=" + active +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String ruleCode;
        private String parameter;
        private Integer threshold;
        private String severity;
        private Boolean active;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder ruleCode(String ruleCode) {
            this.ruleCode = ruleCode;
            return this;
        }

        public Builder parameter(String parameter) {
            this.parameter = parameter;
            return this;
        }

        public Builder threshold(Integer threshold) {
            this.threshold = threshold;
            return this;
        }

        public Builder severity(String severity) {
            this.severity = severity;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public DeviationRule build() {
            return new DeviationRule(id, ruleCode, parameter, threshold, severity, active);
        }
    }
}