package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    private final String errorCode;
    
    // Constructor with just message
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
        this.errorCode = "RESOURCE_NOT_FOUND";
    }
    
    // Constructor with resource details
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", 
                resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.errorCode = "RESOURCE_NOT_FOUND";
    }
    
    // Constructor with resource details and custom error code
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue, String errorCode) {
        super(String.format("%s not found with %s: '%s'", 
                resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.errorCode = errorCode;
    }
    
    // Constructor with custom message and error code
    public ResourceNotFoundException(String message, String errorCode) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
        this.errorCode = errorCode;
    }
    
    // Full constructor
    public ResourceNotFoundException(String message, String resourceName, 
                                   String fieldName, Object fieldValue, String errorCode) {
        super(message);
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.errorCode = errorCode;
    }
    
    // Static factory methods for common use cases
    
    public static ResourceNotFoundException forPatient(Long patientId) {
        return new ResourceNotFoundException(
            "Patient not found",
            "Patient",
            "id",
            patientId,
            "PATIENT_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException forPatientWithPatientId(String patientId) {
        return new ResourceNotFoundException(
            "Patient not found",
            "Patient",
            "patientId",
            patientId,
            "PATIENT_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException forUser(String email) {
        return new ResourceNotFoundException(
            "User not found",
            "User",
            "email",
            email,
            "USER_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException forDailyLog(Long logId) {
        return new ResourceNotFoundException(
            "Daily symptom log not found",
            "DailySymptomLog",
            "id",
            logId,
            "LOG_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException forDailyLogByPatientAndDate(Long patientId, String date) {
        return new ResourceNotFoundException(
            String.format("Daily symptom log not found for patient %s on date %s", patientId, date),
            "DailySymptomLog",
            "patientId_and_date",
            patientId + "_" + date,
            "LOG_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException forRecoveryCurve(Long curveId) {
        return new ResourceNotFoundException(
            "Recovery curve not found",
            "RecoveryCurveProfile",
            "id",
            curveId,
            "CURVE_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException forRecoveryCurveBySurgeryType(String surgeryType) {
        return new ResourceNotFoundException(
            String.format("Recovery curve not found for surgery type: %s", surgeryType),
            "RecoveryCurveProfile",
            "surgeryType",
            surgeryType,
            "CURVE_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException forDeviationRule(Long ruleId) {
        return new ResourceNotFoundException(
            "Deviation rule not found",
            "DeviationRule",
            "id",
            ruleId,
            "RULE_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException forDeviationRuleByCode(String ruleCode) {
        return new ResourceNotFoundException(
            String.format("Deviation rule not found with code: %s", ruleCode),
            "DeviationRule",
            "ruleCode",
            ruleCode,
            "RULE_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException forClinicalAlert(Long alertId) {
        return new ResourceNotFoundException(
            "Clinical alert not found",
            "ClinicalAlertRecord",
            "id",
            alertId,
            "ALERT_NOT_FOUND"
        );
    }
    
    @Override
    public String toString() {
        return String.format("ResourceNotFoundException{resourceName='%s', fieldName='%s', fieldValue='%s', errorCode='%s'}",
                resourceName, fieldName, fieldValue, errorCode);
    }
}