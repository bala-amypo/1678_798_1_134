@Service
public class PatientProfileServiceImpl {

    private final PatientProfileRepository repo;

    public PatientProfileServiceImpl(PatientProfileRepository repo) {
        this.repo = repo;
    }

    public PatientProfile createPatient(PatientProfile p) {
        if (repo.findByEmail(p.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        p.setCreatedAt(LocalDateTime.now());
        return repo.save(p);
    }

    public PatientProfile getPatientById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    public List<PatientProfile> getAllPatients() {
        return repo.findAll();
    }

    public PatientProfile updatePatientStatus(Long id, boolean active) {
        PatientProfile p = getPatientById(id);
        p.setActive(active);
        return repo.save(p);
    }
}
