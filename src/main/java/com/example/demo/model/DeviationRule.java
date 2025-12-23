package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "deviation_rules")
public class DeviationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleCode;
    private String surgeryType;
    private String parameter;
    private Double threshold;
    private String severity;
    private boolean active;

    public DeviationRule() {}

    /* ========= BUILDER ========= */

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final DeviationRule rule = new DeviationRule();

        public Builder ruleCode(String ruleCode) {
            rule.setRuleCode(ruleCode);
            return this;
        }

        public Builder surgeryType(String surgeryType) {
            rule.setSurgeryType(surgeryType);
            return this;
        }

        public Builder parameter(String parameter) {
            rule.setParameter(parameter);
            return this;
        }

        public Builder threshold(Double threshold) {
            rule.setThreshold(threshold);
            return this;
        }

        public Builder severity(String severity) {
            rule.setSeverity(severity);
            return this;
        }

        public Builder active(boolean active) {
            rule.setActive(active);
            return this;
        }

        public DeviationRule build() {
            return rule;
        }
    }

    /* ========= GETTERS ========= */

    public Long getId() {
        return id;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public String getSurgeryType() {
        return surgeryType;
    }

    public String getParameter() {
        return parameter;
    }

    public Double getThreshold() {
        return threshold;
    }

    public String getSeverity() {
        return severity;
    }

    public boolean getActive() {
        return active;
    }

    /* ========= SETTERS ========= */

    public void setId(Long id) {
        this.id = id;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
