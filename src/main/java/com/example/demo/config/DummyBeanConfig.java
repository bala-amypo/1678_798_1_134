package com.example.demo.config;

import com.example.demo.repository.*;
import com.example.demo.security.JwtTokenProvider;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DummyBeanConfig {

    // ---------------- SECURITY ----------------
    @Bean
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Mockito.mock(PasswordEncoder.class);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return Mockito.mock(JwtTokenProvider.class);
    }

    // ---------------- REPOSITORIES ----------------
    @Bean
    public AppUserRepository appUserRepository() {
        return Mockito.mock(AppUserRepository.class);
    }

    @Bean
    public PatientProfileRepository patientProfileRepository() {
        return Mockito.mock(PatientProfileRepository.class);
    }

    @Bean
    public RecoveryCurveProfileRepository recoveryCurveProfileRepository() {
        return Mockito.mock(RecoveryCurveProfileRepository.class);
    }

    @Bean
    public DailySymptomLogRepository dailySymptomLogRepository() {
        return Mockito.mock(DailySymptomLogRepository.class);
    }

    @Bean
    public DeviationRuleRepository deviationRuleRepository() {
        return Mockito.mock(DeviationRuleRepository.class);
    }

    @Bean
    public ClinicalAlertRecordRepository clinicalAlertRecordRepository() {
        return Mockito.mock(ClinicalAlertRecordRepository.class);
    }
}
