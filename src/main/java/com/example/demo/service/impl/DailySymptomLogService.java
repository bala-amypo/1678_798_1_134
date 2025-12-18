@Service
public class DailySymptomLogServiceImpl {

    private final DailySymptomLogRepository repo;

    public DailySymptomLogServiceImpl(DailySymptomLogRepository repo) {
        this.repo = repo;
    }

    public DailySymptomLog recordLog(DailySymptomLog log) {
        if (log.getLogDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("future date");
        }
        log.setSubmittedAt(LocalDateTime.now());
        return repo.save(log);
    }
}
