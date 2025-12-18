@Service
public class DeviationRuleServiceImpl {

    private final DeviationRuleRepository repo;

    public DeviationRuleServiceImpl(DeviationRuleRepository repo) {
        this.repo = repo;
    }

    public DeviationRule createRule(DeviationRule rule) {
        if (rule.getThresholdDeviation() == null || rule.getThresholdDeviation() <= 0) {
            throw new IllegalArgumentException("Threshold must be positive");
        }
        return repo.save(rule);
    }
}
