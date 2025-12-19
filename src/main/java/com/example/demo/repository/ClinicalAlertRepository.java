package com.example.demo.repository;

import com.example.demo.model.ClinicalAlertRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClinicalAlertRecordRepository
        extends JpaRepository<ClinicalAlertRecord, Long> {

    List<ClinicalAlertRecord> findByPatientId(Long patientId);
}
package com.example.demo.repository;

/**
 * Dummy placeholder repository.
 *
 * IMPORTANT:
 * - This file exists only because it cannot be deleted.
 * - The real JPA repository used by the application is:
 *   ClinicalAlertRecordRepository
 * - This interface MUST NOT be public.
 * - It MUST NOT extend JpaRepository.
 */
interface ClinicalAlertRepository {
    // intentionally empty
}
