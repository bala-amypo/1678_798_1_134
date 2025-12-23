package com.example.demo.model;

public class DeviationRule {

    private Long id;
    private String ruleCode;
    private String surgeryType;
    private String parameter;
    private Integer threshold;
    private String severity;
    private boolean active = true;

    public DeviationRule() {
    }

    // ---------- GETTERS / SETTERS ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }

    public String getSurgeryType() { return surgeryType; }
    public void setSurgeryType(String surgeryType) { this.surgeryType = surgeryType; }

    public String getParameter() { return parameter; }
    public void setParameter(String parameter) { this.parameter = parameter; }

    public Integer getThreshold() { return threshold; }
    public void setThreshold(Integer threshold) { this.threshold = threshold; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public boolean getActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
