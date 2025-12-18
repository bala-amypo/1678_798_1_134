public interface DeviationRuleRepository extends JpaRepository<DeviationRule, Long> {
    List<DeviationRule> findBySurgeryType(String surgeryType);
}
