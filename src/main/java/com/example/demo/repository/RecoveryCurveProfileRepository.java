package com.example.demo.repository;

import com.example.demo.model.RecoveryCurveProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecoveryCurveProfileRepository extends JpaRepository<RecoveryCurveProfile, Long> {
    
    // Find by surgery type ordered by day number
    List<RecoveryCurveProfile> findBySurgeryTypeOrderByDayNumberAsc(String surgeryType);
    
    // Find by surgery type and day number range
    List<RecoveryCurveProfile> findBySurgeryTypeAndDayNumberBetween(
            String surgeryType, 
            Integer startDay, 
            Integer endDay);
    
    // Find specific curve entry by surgery type and day number
    Optional<RecoveryCurveProfile> findBySurgeryTypeAndDayNumber(String surgeryType, Integer dayNumber);
    
    // Find curves for multiple surgery types
    List<RecoveryCurveProfile> findBySurgeryTypeIn(List<String> surgeryTypes);
    
    // Find curves by recovery phase
    List<RecoveryCurveProfile> findByRecoveryPhase(String recoveryPhase);
    
    // Find curves by surgery type and recovery phase
    List<RecoveryCurveProfile> findBySurgeryTypeAndRecoveryPhase(String surgeryType, String recoveryPhase);
    
    // Find curves with expected pain above threshold
    @Query("SELECT r FROM RecoveryCurveProfile r WHERE r.expectedPainLevel >= :threshold")
    List<RecoveryCurveProfile> findCurvesWithHighExpectedPain(@Param("threshold") Integer threshold);
    
    // Find curves with expected mobility above threshold
    @Query("SELECT r FROM RecoveryCurveProfile r WHERE r.expectedMobilityLevel >= :threshold")
    List<RecoveryCurveProfile> findCurvesWithHighExpectedMobility(@Param("threshold") Integer threshold);
    
    // Get distinct surgery types
    @Query("SELECT DISTINCT r.surgeryType FROM RecoveryCurveProfile r ORDER BY r.surgeryType")
    List<String> findDistinctSurgeryTypes();
    
    // Get maximum day number for surgery type
    @Query("SELECT MAX(r.dayNumber) FROM RecoveryCurveProfile r WHERE r.surgeryType = :surgeryType")
    Optional<Integer> findMaxDayNumberBySurgeryType(@Param("surgeryType") String surgeryType);
    
    // Get curves for acute phase (first 2 weeks)
    @Query("SELECT r FROM RecoveryCurveProfile r WHERE r.dayNumber <= 14 AND r.surgeryType = :surgeryType ORDER BY r.dayNumber")
    List<RecoveryCurveProfile> findAcutePhaseCurves(@Param("surgeryType") String surgeryType);
    
    // Get curves for subacute phase (3-6 weeks)
    @Query("SELECT r FROM RecoveryCurveProfile r WHERE r.dayNumber >= 15 AND r.dayNumber <= 42 AND r.surgeryType = :surgeryType ORDER BY r.dayNumber")
    List<RecoveryCurveProfile> findSubacutePhaseCurves(@Param("surgeryType") String surgeryType);
    
    // Get curves for chronic phase (after 6 weeks)
    @Query("SELECT r FROM RecoveryCurveProfile r WHERE r.dayNumber > 42 AND r.surgeryType = :surgeryType ORDER BY r.dayNumber")
    List<RecoveryCurveProfile> findChronicPhaseCurves(@Param("surgeryType") String surgeryType);
    
    // Find curves with specific day numbers
    List<RecoveryCurveProfile> findByDayNumberIn(List<Integer> dayNumbers);
    
    // Calculate average expected values by surgery type
    @Query("SELECT r.surgeryType, AVG(r.expectedPainLevel), AVG(r.expectedMobilityLevel), AVG(r.expectedFatigueLevel) " +
           "FROM RecoveryCurveProfile r GROUP BY r.surgeryType")
    List<Object[]> calculateAverageExpectedValuesBySurgeryType();
    
    // Find curves with recommendations containing keyword
    @Query("SELECT r FROM RecoveryCurveProfile r WHERE LOWER(r.recommendations) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<RecoveryCurveProfile> findByRecommendationsKeyword(@Param("keyword") String keyword);
    
    // Find curves with warning signs containing keyword
    @Query("SELECT r FROM RecoveryCurveProfile r WHERE LOWER(r.warningSigns) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<RecoveryCurveProfile> findByWarningSignsKeyword(@Param("keyword") String keyword);
    
    // Find curves for specific day ranges
    @Query("SELECT r FROM RecoveryCurveProfile r WHERE r.surgeryType = :surgeryType AND r.dayNumber BETWEEN :minDay AND :maxDay ORDER BY r.dayNumber")
    List<RecoveryCurveProfile> findBySurgeryTypeAndDayRange(
            @Param("surgeryType") String surgeryType,
            @Param("minDay") Integer minDay,
            @Param("maxDay") Integer maxDay);
}