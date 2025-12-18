public interface ClinicalAlertRepository extends JpaRepository<ClinicalAlert, Long> {
    List<ClinicalAlert> findByPatientid(Long patientid);
}
