public interface DailySymptomLogRepository extends JpaRepository<DailySymptomLog, Long> {
    List<DailySymptomLog> findByPatientid(Long patientid);
}
