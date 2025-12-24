package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.PatientProfile;
import com.example.demo.repository.PatientProfileRepository;
import com.example.demo.service.PatientProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientProfileRepository repository;

    public PatientProfileServiceImpl(PatientProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public PatientProfile createProfile(PatientProfile profile) {
        return repository.save(profile);
    }

    @Override
    public PatientProfile updateProfile(Long id, PatientProfile profile) {
        PatientProfile existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        existing.setFullName(profile.getFullName());
        existing.setAge(profile.getAge());
        existing.setGender(profile.getGender());
        existing.setSurgeryType(profile.getSurgeryType());
        existing.setSurgeryDate(profile.getSurgeryDate());

        return repository.save(existing);
    }

    @Override
    public Optional<PatientProfile> getProfileById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<PatientProfile> getAllProfiles() {
        return repository.findAll();
    }

    @Override
    public void deleteProfile(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Patient profile not found");
        }
        repository.deleteById(id);
    }
}
