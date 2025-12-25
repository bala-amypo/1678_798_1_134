package com.example.demo.repository;

import com.example.demo.model.ClinicalAlertRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClinicalAlertRecordRepository extends JpaRepository<ClinicalAlertRecord, Long> {
    
    // Find alerts by patient
    List<ClinicalAlertRecord> findByPatientId(Long patientId);
    
    // Find unresolved alerts by patient
    List<ClinicalAlertRecord> findByPatientIdAndResolvedFalse(Long patientId);
    
    // Find all unresolved alerts
    List<ClinicalAlertRecord> findByResolvedFalse();
    
    // Find alerts by severity
    List<ClinicalAlertRecord> findBySeverity(String severity);
    
    // Find unresolved alerts by severity
    List<ClinicalAlertRecord> findBySeverityAndResolvedFalse(String severity);
    
    // Find alerts by alert type
    List<ClinicalAlertRecord> findByAlertType(String alertType);
    
    // Find unresolved alerts by alert type
    List<ClinicalAlertRecord> findByAlertTypeAndResolvedFalse(String alertType);
    
    // Find alerts by parameter
    List<ClinicalAlertRecord> findByParameter(String parameter);
    
    // Find alerts created after specific date
    List<ClinicalAlertRecord> findByCreatedAtAfter(LocalDateTime date);
    
    // Find alerts created within date range
    List<ClinicalAlertRecord> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Find high priority unresolved alerts
    @Query("SELECT a FROM ClinicalAlertRecord a WHERE a.resolved = false AND a.severity IN ('HIGH', 'CRITICAL') ORDER BY a.createdAt DESC")
    List<ClinicalAlertRecord> findHighPriorityUnresolvedAlerts();
    
    // Count unresolved alerts by patient
    @Query("SELECT COUNT(a) FROM ClinicalAlertRecord a WHERE a.patientId = :patientId AND a.resolved = false")
    Long countUnresolvedAlertsByPatient(@Param("patientId") Long patientId);
    
    // Find unacknowledged alerts
    List<ClinicalAlertRecord> findByResolvedFalseAndAcknowledgedFalse();
    
    // Find alerts acknowledged by specific user
    List<ClinicalAlertRecord> findByAcknowledgedBy(Long userId);
    
    // Find alerts resolved by specific user
    List<ClinicalAlertRecord> findByResolvedBy(Long userId);
    
    // Find alerts by rule
    List<ClinicalAlertRecord> findByRuleId(Long ruleId);
    
    // Find alerts by log
    List<ClinicalAlertRecord> findByLogId(Long logId);
    
    // Calculate alert statistics
    @Query("SELECT a.severity, COUNT(a) FROM ClinicalAlertRecord a WHERE a.createdAt >= :startDate GROUP BY a.severity")
    List<Object[]> countAlertsBySeveritySince(@Param("startDate") LocalDateTime startDate);
    
    // Find alerts requiring immediate attention
    @Query("SELECT a FROM ClinicalAlertRecord a WHERE a.resolved = false AND (a.severity = 'CRITICAL' OR (a.severity = 'HIGH' AND a.acknowledged = false))")
    List<ClinicalAlertRecord> findAlertsRequiringImmediateAttention();
    
    // Find alerts with notification sent
    List<ClinicalAlertRecord> findByNotificationSentTrue();
    
    // Find alerts by escalation level
    List<ClinicalAlertRecord> findByEscalationLevel(Integer escalationLevel);
    
    // Find alerts with high deviation amount
    @Query("SELECT a FROM ClinicalAlertRecord a WHERE a.deviationAmount >= :threshold")
    List<ClinicalAlertRecord> findAlertsWithHighDeviation(@Param("threshold") Integer threshold);
    
    // Calculate average time to resolution
    @Query("SELECT AVG(EXTRACT(EPOCH FROM (a.resolvedAt - a.createdAt))/3600) FROM ClinicalAlertRecord a WHERE a.resolved = true AND a.resolvedAt IS NOT NULL")
    Double calculateAverageResolutionTimeHours();
    
    // Find alerts for multiple patients
    @Query("SELECT a FROM ClinicalAlertRecord a WHERE a.patientId IN :patientIds AND a.resolved = false ORDER BY a.severity DESC, a.createdAt DESC")
    List<ClinicalAlertRecord> findUnresolvedAlertsForPatients(@Param("patientIds") List<Long> patientIds);
    
    // Find alerts with specific notification channel
    @Query("SELECT a FROM ClinicalAlertRecord a WHERE a.notificationChannels LIKE %:channel%")
    List<ClinicalAlertRecord> findByNotificationChannel(@Param("channel") String channel);
}