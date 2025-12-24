package com.example.demo.config;

import com.example.demo.repository.*;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Proxy;

@Configuration
public class DummyBeanConfig {

    // ---------- GENERIC PROXY ----------
    @SuppressWarnings("unchecked")
    private <T> T dummy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> {
                    // return sensible defaults
                    if (method.getReturnType().isPrimitive()) {
                        return 0;
                    }
                    return null;
                }
        );
    }

    // ---------- SECURITY ----------
    @Bean
    public AuthenticationManager authenticationManager() {
        return dummy(AuthenticationManager.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return dummy(PasswordEncoder.class);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider("dummy-secret-key-dummy-secret-key");
    }

    // ---------- REPOSITORIES ----------
    @Bean
    public AppUserRepository appUserRepository() {
        return dummy(AppUserRepository.class);
    }

    @Bean
    public PatientProfileRepository patientProfileRepository() {
        return dummy(PatientProfileRepository.class);
    }

    @Bean
    public RecoveryCurveProfileRepository recoveryCurveProfileRepository() {
        return dummy(RecoveryCurveProfileRepository.class);
    }

    @Bean
    public DailySymptomLogRepository dailySymptomLogRepository() {
        return dummy(DailySymptomLogRepository.class);
    }

    @Bean
    public DeviationRuleRepository deviationRuleRepository() {
        return dummy(DeviationRuleRepository.class);
    }

    @Bean
    public ClinicalAlertRecordRepository clinicalAlertRecordRepository() {
        return dummy(ClinicalAlertRecordRepository.class);
    }
}
