public interface RecoveryCurveProfileRepository extends JpaRepository<RecoveryCurveProfile, Long> {
    List<RecoveryCurveProfile> findBySurgeryType(String surgeryType);
}
